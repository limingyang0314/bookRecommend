package com.example

import java.util.Calendar

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

class UserBookRating(spark: SparkSession) {

  def generateUserBookRating(year: Int, month: Int, day: Int): Unit = {

    spark.udf.register("sigmoid",
      (score: Double) => {
        2 * 1 / (1 + math.exp(-score))
      }
    )

    val tmp = spark.sql(
      "select user_id, book_id, " +
      "sigmoid(sum(score)) as rating " +
      "from book_recommend.user_behavior_score " +
      // "where year='"+ year + "' " +
      // "and month='"+ month + "' " +
      // "and day='"+ day + "' " +
      "group by user_id, book_id")

    tmp.createOrReplaceTempView("rate_table")

    spark.sql("insert overwrite " +
      "table book_recommend.user_book_rating " +
      "select *, "+
      year +" as year, " +
      month +" as month, " +
      day +" as day from rate_table")
  }
}

object UserBookRating {
  def apply(spark: SparkSession): UserBookRating = new UserBookRating(spark)
}
