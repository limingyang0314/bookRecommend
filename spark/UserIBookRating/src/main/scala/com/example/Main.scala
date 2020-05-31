package com.example

import java.util.Calendar

import org.apache.spark.sql.SparkSession

object Main {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      //.master("yarn-cluster")
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DATE)

    val behaviorScore = BehaviorScore(spark)
    behaviorScore.generateBehaviorScore(year, month, day)

//    val userBookRating = UserBookRating(spark)
//    userBookRating.generateUserBookRating(year, month, day)

  }

}
