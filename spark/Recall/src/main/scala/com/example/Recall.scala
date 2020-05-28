package com.example

import java.text.SimpleDateFormat
import java.util.Date

import com.example.utils.ModelUtil
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.ml.recommendation.ALSModel
import org.apache.spark.sql.{DataFrame, SparkSession}

object Recall {

  val colALS = "als"

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("recall")
      //.master("yarn-cluster")
      .master("local")
      .enableHiveSupport()
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    val modelUtil = ModelUtil(spark)
    val data: DataFrame = modelUtil.getUserItemRating


    val als = ALSRecall(data)

    val maxIter = 10

    val reg = Array(0.1, 0.05, 0.0001)

    val rank = Array(20, 50, 80, 100)

    val alpha = Array(2.0, 3.0)

    val model: ALSModel = als.getModel(maxIter, rank, reg, alpha)
    val alsRecallData = als.getALSRecall(model, spark)

    // modelUtil.saveRecall(alsRecallData, colALS, "user")


    val colItem2Item = "item2item"

    // val format = new SimpleDateFormat("yyyy-MM-dd")
    // val date = format.format(new Date())
    // val path = "./model/als_model/" + date + "/itemFactors"
    // val itemFactors = spark.read.parquet(path)

    val item2Item = Item2ItemRecall()

    val itemCosSim = item2Item.getCosSim(model, spark)

    val itemCosSimBd: Broadcast[DataFrame] = spark.sparkContext.broadcast(itemCosSim)

    val item2ItemRecallData = item2Item.getItem2ItemRecall(data, itemCosSimBd, spark)


    modelUtil.saveRecall(alsRecallData, item2ItemRecallData, colItem2Item, "user", "overwrite")


    spark.stop()
  }
}
