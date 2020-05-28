package com.example.utils

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection.mutable
import org.apache.spark.sql.functions._

import scala.collection.mutable.ArrayBuffer

class ModelUtil(spark: SparkSession) {

  def getDF: DataFrame = {
    val jdbcDF = spark.read.format("jdbc")
      .option("url", "jdbc:mysql://rm-bp14h269ydw6qq5h50o.mysql.rds.aliyuncs.com:3306/book_recommend?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai")
      .option("driver", "com.mysql.cj.jdbc.Driver")
      .option("dbtable", "ratings")
      .option("user", "book_recommend")
      .option("password", "dawangba1A")
      .load()
      .where(col("rating") >= 4)
      .groupBy(col("user_id"))
      .agg(collect_list("book_id"))
      .withColumnRenamed("collect_list(book_id)",
        "books")
      .select("books")
    jdbcDF
  }

  def getBooks: RDD[Seq[Long]] = {

//    val sc = spark.sparkContext
//    val data = sc.textFile("./test/books.txt")
//
//    val books = data.map {
//      x =>
//        val _x = x.split(",")
//        val set = mutable.Set[Int]()
//        for(i <- 0 until _x.length){
//          set.add(_x(i).toInt)
//        }
//        set.toSeq
//    }

    val books = getDF.rdd
        .map(row => {
          row.getAs[Seq[Long]](0)
        })

    books
  }



  def saveRecommend(list: List[Seq[Long]], saveModel: String): Unit = {

    val arrToString = udf((value: Seq[Long]) => {
      val set = mutable.Set[Long]()
      for (i <- value.indices) {
        set.add(value(i))
      }
      set.mkString(",")
    })

    import spark.implicits._

    val recommendList = list.flatMap(_.combinations(2))
      .flatMap(x => {
        val seq = ArrayBuffer[(Long, Long)]()
        seq += ((x.head, x(1)))
        seq += ((x(1), x.head))
        seq
      })
      .toDF("book_id", "other")
      .groupBy(col("book_id"))
      .agg(collect_list("other"))
      .withColumnRenamed("collect_list(other)",
        "books")
      .withColumn("books", arrToString(col("books")))
      .select(col("book_id"),
        col("books"))

    import java.util.Properties

    val prop = new Properties()
    prop.put("user", "book_recommend")
    prop.put("password", "dawangba1A")
    prop.put("driver", "com.mysql.cj.jdbc.Driver")


    recommendList.write.mode(saveModel)
      .jdbc(
        "jdbc:mysql://rm-bp14h269ydw6qq5h50o.mysql.rds.aliyuncs.com:3306/book_recommend?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai",
        "books_recommend",
        prop
      )

  }
}

object ModelUtil{
  var spark: SparkSession = _
  def apply(spark: SparkSession): ModelUtil = new ModelUtil(spark)
}

