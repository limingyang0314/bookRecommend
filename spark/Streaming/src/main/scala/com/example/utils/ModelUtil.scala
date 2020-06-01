package com.example.utils

import java.util
import java.util.UUID

import org.apache.spark.sql.functions._
import com.example.model.EventModel
import org.apache.spark.ml.classification.{LogisticRegression, LogisticRegressionModel}
import org.apache.spark.sql.{DataFrame, DataFrameReader, Dataset, Row, SparkSession}

import scala.collection.mutable

class ModelUtil(spark: SparkSession) {

  val jdbcDF: DataFrameReader = spark.read.format("jdbc")
    .option("url", "jdbc:mysql://rm-bp14h269ydw6qq5h50o.mysql.rds.aliyuncs.com:3306/book_recommend?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai")
    .option("driver", "com.mysql.cj.jdbc.Driver")
    .option("user", "book_recommend")
    .option("password", "dawangba1A")

  def saveAction(list: util.List[EventModel]): Unit = {
    val value = mutable.Seq[EventModel]()
    for (i <- 0 until list.size()) {
      value :+ list.get(i)
    }

    import spark.implicits._
    val data = value.map(x => {
      val uuid = UUID.randomUUID().toString
      (uuid, x.getUserId, x.getBookId, x.getTagIds, x.getAction, x.getYear, x.getMonth, x.getDay, x.getHour)
    }).toDF("user_id", "book_is", "tagids", "action", "year", "month", "day", "hour")

    data.write.mode("append").insertInto("online_action")
  }

  def writeToDB(res: DataFrame): Unit = {
     import java.util.Properties
     val prop = new Properties()
     prop.put("user", "book_recommend")
     prop.put("password", "dawangba1A")
     prop.put("driver", "com.mysql.cj.jdbc.Driver")
     res.write.mode("append")
       .jdbc(
         "jdbc:mysql://rm-bp14h269ydw6qq5h50o.mysql.rds.aliyuncs.com:3306/book_recommend?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai",
         "users_recall",
         prop
       )
  }

  def lrRank(list: Dataset[Row]): Dataset[Row] = {
    val modelPath = "/model/lr/lr.obj"
    val lrModel = LogisticRegressionModel.load(modelPath)
    lrModel.transform(list)
  }

  def recommendUser(data: DataFrame): Unit = {

    val userRecall = jdbcDF.option("dbtable", "users_recommend")
        .load()

    val bookRecall = jdbcDF.option("dbtable", "books_recommend")
      .load()

    val books = spark.sql("select * from book_recommend.user_book_rating")

    val list = data.join(bookRecall, Seq[String]("book_id")).join(userRecall, "user_id")
      .join(books, Seq[String]("book_id", "user_id"))
      .orderBy(col("score").desc).limit(50)

    val res = lrRank(list)

    writeToDB(res)
  }


}

object ModelUtil {
  def apply(spark: SparkSession): ModelUtil = new ModelUtil(spark)
}
