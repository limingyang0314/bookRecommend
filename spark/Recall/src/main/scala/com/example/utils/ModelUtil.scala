package com.example.utils

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._

import scala.collection.mutable

class ModelUtil(spark: SparkSession) {

  val rsRecall = "history_rs_recall"

  val cf = "recall"

  def getUserItemRating: DataFrame = {

    // val data = spark.sql (
    //   "select * from book_recommend.ratings")
    // data

    val jdbcDF = spark.read.format("jdbc")
      .option("url", "jdbc:mysql://rm-bp14h269ydw6qq5h50o.mysql.rds.aliyuncs.com:3306/book_recommend?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai")
      .option("driver", "com.mysql.cj.jdbc.Driver")
      .option("dbtable", "ratings")
      .option("user", "book_recommend")
      .option("password", "dawangba1A")
      .load()

    jdbcDF
  }

  def saveRecall(recommend: DataFrame, recommend2: DataFrame, cell: String, name: String, saveModel: String): Unit = {
    val idName = name + "_id"
    /**
     *  user_id     itemid
     *  3       [12,34,24,89,21]
     *  5       [19,78,67,21,12]
     */


    val arrToString = udf((value: Seq[Int], value2: Seq[Int]) => {
      val set = mutable.Set[Int]()
      for (i <- value.indices) {
        set.add(value(i))
      }
      for (i <- value2.indices) {
        set.add(value2(i))
      }
      set.mkString(",")
    })

    val recommendList2 = recommend2.groupBy(col(idName))
      .agg(collect_list("book_id"))
      .withColumnRenamed("collect_list(book_id)",
        "books2")
      .select(col(idName),
        col("books")
      )


    val recommendList = recommend.groupBy(col(idName))
      .agg(collect_list("book_id"))
      .withColumnRenamed("collect_list(book_id)",
        "books1")
      .select(col(idName),
        col("books1"))
      .join(recommendList2, "user_id")
      .withColumn("books", arrToString(col("books1"), col("books2")))
      .select(col(idName),
        col("books"))

    import java.util.Properties

    val prop = new Properties()
    prop.put("user", "book_recommend")
    prop.put("password", "dawangba1A")
    prop.put("driver", "com.mysql.cj.jdbc.Driver")


    recommendList.write.mode(saveModel)
      .jdbc(
        "jdbc:mysql://rm-bp14h269ydw6qq5h50o.mysql.rds.aliyuncs.com:3306/book_recommend?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai",
        name + "s_recommend",
        prop
      )

     // val HBase = new HBaseUtil(spark)
     // HBase.putData(rsRecall, recommendList, cf, cell)

  }

}

object ModelUtil{
  var spark: SparkSession = _
  def apply(spark: SparkSession): ModelUtil = new ModelUtil(spark)
}

