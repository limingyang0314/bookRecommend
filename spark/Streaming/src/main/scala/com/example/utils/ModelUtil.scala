package com.example.utils

import java.util
import java.util.UUID

import com.example.model.EventModel
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection.mutable

class ModelUtil(spark: SparkSession) {

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

  def recommendUser(data: DataFrame): Unit = {
      val jdbcDF = spark.read.format("jdbc")
        .option("url", "jdbc:mysql://rm-bp14h269ydw6qq5h50o.mysql.rds.aliyuncs.com:3306/book_recommend?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai")
        .option("driver", "com.mysql.cj.jdbc.Driver")
        .option("dbtable", "users_recommend")
        .option("user", "book_recommend")
        .option("password", "dawangba1A")
        .load()

    val book_recall = spark.read.format("jdbc")
      .option("url", "jdbc:mysql://rm-bp14h269ydw6qq5h50o.mysql.rds.aliyuncs.com:3306/book_recommend?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai")
      .option("driver", "com.mysql.cj.jdbc.Driver")
      .option("dbtable", "books_recommend")
      .option("user", "book_recommend")
      .option("password", "dawangba1A")
      .load()

    val list = jdbcDF.join(book_recall, Seq[String]("book_id"))

      val books = spark.sql("select * from book_recommend.user_book_rating")
        .join(data, Seq[String]("book_id", "user_id"))
  }

}

object ModelUtil {
  def apply(spark: SparkSession): ModelUtil = new ModelUtil(spark)
}
