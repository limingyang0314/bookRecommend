package com.example

import com.example.utils.ModelUtil
import org.apache.spark.ml.recommendation.ALSModel
import org.apache.spark.sql.{DataFrame, SparkSession}

object Recall {

  val colALS = "als"

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("recall")
      .master("yarn-cluster")
      .enableHiveSupport()
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    val modelUtil = ModelUtil(spark)
    val data: DataFrame = modelUtil.getUserItemRating


    val als = ALSRecall(data)

    val maxIter = 10

    val reg = Array(0.1, 0.05, 0.0001)

    val rank = Array(20, 50, 80, 100)

    val alpha = Array(2.0)

    val model:ALSModel = als.getModel(maxIter, rank, reg, alpha)
    val alsRecallData = als.getALSRecall(model, spark)

    modelUtil.saveRecall(alsRecallData, colALS)

    spark.stop()
  }
}
