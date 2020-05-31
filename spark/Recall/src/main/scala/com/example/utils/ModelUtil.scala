package com.example.utils

import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StructType

import scala.collection.mutable

case class ResultInfo(user_id: Long, book_id: Long)

class ModelUtil(spark: SparkSession) {

  val HBase = new HBaseUtil(spark)
  val rsRecall = "history_recall"

  val cf = "recall"

  def getTextData: DataFrame = {

      val data = spark.read.format("csv")
      .option("delimiter", ",")
      .option("inferschema", true)
      .schema(ScalaReflection.schemaFor[ResultInfo].dataType.asInstanceOf[StructType])
      .load("file:///G:/big-data/BookRecommend/test/hbase.txt")

      data
  }

  def getUserItemRating: DataFrame = {

     val data = spark.sql (
       "select * from book_recommend.user_book_rating")
     data

//    val jdbcDF = spark.read.format("jdbc")
//      .option("url", "jdbc:mysql://rm-bp14h269ydw6qq5h50o.mysql.rds.aliyuncs.com:3306/book_recommend?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai")
//      .option("driver", "com.mysql.cj.jdbc.Driver")
//      .option("dbtable", "ratings")
//      .option("user", "book_recommend")
//      .option("password", "dawangba1A")
//      .load()
//
//    jdbcDF
  }

  def collectData(recommend: DataFrame): DataFrame = {
    val list = recommend.groupBy(col("user_id"))
      .agg(collect_list("book_id"))
      .withColumnRenamed("collect_list(book_id)",
        "books")
      .select(col("user_id"),
        col("books")
      )

    list
  }

  def saveRecallMySQL(recommend: DataFrame, recommend2: DataFrame, saveModel: String): Unit = {
    val idName = "user_id"
    /**
     *  user_id     itemid
     *  3       [12,34,24,89,21]
     *  5       [19,78,67,21,12]
     */


    val intersectToString = udf((value: Seq[Long], value2: Seq[Long]) => {
      val set = mutable.Set[Long]()
      val res = mutable.Set[Long]()

      for (i <- value.indices) {
        set.add(value(i))
      }
      for (i <- value2.indices) {
        if (!set.add(value2(i))) {
          res.add(value2(i))
        }

      }
      res.mkString(",")
    })

    val recommendList = collectData(recommend)
      .withColumnRenamed("books",
        "books1")
    val recommendList2 = collectData(recommend2)
      .withColumnRenamed("books",
        "books2")

    val recommendData = recommendList
      .join(recommendList2, "user_id")
      .withColumn("books", intersectToString(col("books1"), col("books2")))
      .select(col(idName),
        col("books"))

    // HBase.putData("user_recall", recommendData, cf, "recall")

    // import java.util.Properties
    // val prop = new Properties()
    // prop.put("user", "book_recommend")
    // prop.put("password", "dawangba1A")
    // prop.put("driver", "com.mysql.cj.jdbc.Driver")
    // recommendData.write.mode(saveModel)
    //   .jdbc(
    //     "jdbc:mysql://rm-bp14h269ydw6qq5h50o.mysql.rds.aliyuncs.com:3306/book_recommend?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai",
    //     "users_recall",
    //     prop
    //   )

    recommendData.write.mode("overwrite")
        .insertInto("book_recommend.users_recall")

  }



  def saveRecall(recommend: DataFrame, cell: String): Unit = {

    val arrToString = udf((value: Seq[Long]) => {
      value.mkString(",")
    })

//    val arrToString = udf((value: Seq[Int]) => {
//      value.mkString(",")
//    })

    val recommendList = collectData(recommend)
        .withColumn("books", arrToString(col("books")))
     HBase.putData(rsRecall, recommendList, cf, cell)
  }

}

object ModelUtil{
  var spark: SparkSession = _
  def apply(spark: SparkSession): ModelUtil = new ModelUtil(spark)
}

