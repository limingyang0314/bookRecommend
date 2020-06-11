package com.example

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.spark.sql.functions._
import com.example.utils.ModelUtil
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object Rank {

  def main(array: Array[String]): Unit = {

    val spark = SparkSession.builder()
      //.master("yarn-cluster")
      .master("local")
      .enableHiveSupport()
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")


    val modelUtil = ModelUtil(spark)
    val training = modelUtil.getFeature()
    val lr = LRModel(spark)
    val lrModel = lr.getModel(training)

    val data = lr.getRank(lrModel)

    modelUtil.saveResult(data)

  }
}
