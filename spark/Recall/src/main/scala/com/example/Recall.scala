package com.example

import java.text.SimpleDateFormat

import com.example.utils.ModelUtil
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.ml.recommendation.ALSModel
import org.apache.spark.sql.{DataFrame, SparkSession}


object Recall {

  val colALS = "als"
  val colItem2Item = "item2item"

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

    // val data = modelUtil.getTextData
    // modelUtil.saveRecallMySQL(data, data, "append")
    // modelUtil.saveRecall(data, colALS)
    // modelUtil.saveRecall(data, colItem2Item)

    val als = ALSRecall(data)

    val maxIter = 10

    val reg = Array(0.1, 0.05, 0.0001)

    val rank = Array(20, 50, 80, 100)

    val alpha = Array(2.0, 3.0)

    val model: ALSModel = als.getModel(maxIter, rank, reg, alpha)
    val alsRecallData = als.getALSRecall(model, spark)
    // modelUtil.saveRecall(alsRecallData, colALS)

    // val format = new SimpleDateFormat("yyyy-MM-dd")
    // val date = format.format(new Date())
    // val path = "file:///G:/big-data/BookRecommend/model/als_model/2020-05-28/itemFactors"
    // val itemFactors = spark.read.parquet(path)
    //  itemFactors.show()

    val item2Item = Item2ItemRecall()

    val itemCosSim = item2Item.getCosSim(model, spark)

    val itemCosSimBd: Broadcast[DataFrame] = spark.sparkContext.broadcast(itemCosSim)

    val item2ItemRecallData = item2Item.getItem2ItemRecall(data, itemCosSimBd, spark)
     modelUtil.saveRecall(alsRecallData, colItem2Item)

     modelUtil.saveRecallMySQL(alsRecallData, item2ItemRecallData, "append")


    spark.stop()
  }
}
