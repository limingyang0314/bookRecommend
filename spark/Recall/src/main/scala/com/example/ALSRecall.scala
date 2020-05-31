package com.example

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.recommendation.{ALS, ALSModel}
import org.apache.spark.sql.functions.{col, explode}
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection.mutable.ArrayBuffer


class ALSRecall(data: DataFrame) {

  def getEvaluate(predict: DataFrame): Double = {

    val re = new RegressionEvaluator()
      .setMetricName("rmse")
      .setLabelCol("rating")
      .setPredictionCol("prediction")

    val rmse = re.evaluate(predict)
    rmse
  }

  def getModel(maxIter: Int,
               rankArray: Array[Int],
               regArray: Array[Double],
               alphaArray: Array[Double]): ALSModel = {

    val Array(training, test) =
      data.randomSplit(Array(0.8, 0.2))

    var mapModel = Map[Double, ALSModel]()
    val listMSE = ArrayBuffer[Double]()

    for (rank <- rankArray;
        reg <- regArray;
        alpha <- alphaArray) {
      val als = new ALS()
        .setMaxIter(maxIter)
        .setUserCol("user_id")
        .setItemCol("book_id")
        .setRatingCol("rating")
        .setRank(rank)
        .setRegParam(reg)
        .setImplicitPrefs(false)
        .setAlpha(alpha)

      val model = als.fit(training)

      // handle cold start
      model.setColdStartStrategy("drop")
      val predict = model.transform(test)

      val rmse = getEvaluate(predict)

      listMSE += rmse
      mapModel += (rmse -> model)

    }

    val minMSE = listMSE.min
    val bestModel = mapModel(minMSE)

    val format = new SimpleDateFormat("yyyy-MM-dd")
    val date = format.format(new Date())
    val modelPath = "/model/als_model/" + date
    bestModel.write.overwrite().save(modelPath)

    bestModel

  }

  def getALSRecall(model: ALSModel,
                   spark: SparkSession): DataFrame = {
    val list = model.recommendForAllUsers(20)

    /**
     *  uid  recommendations
     *  2    [[34,0.782],[56,0.94224],[78,0.4294]]
     *
     * uid  recommend
     *  2   [34,0.782]
     *  2   [56,0.94224]
     *  2   [78,0.4294]
     *
     *   uid  itemid
     * *  2     34
     * *  2     56
     * *  2     78
     *
     */

    import spark.implicits._
    val recallData = list.withColumn("recommend",
      explode(col("recommendations")))
      .drop("recommendations")
      .select("user_id","recommend")
      .rdd.map(row => {
      val uid = row.getInt(0)
      val recommend = row.getStruct(1)
      val itemid = recommend.getAs[Int]("book_id")
      (uid, itemid)
    }).toDF("user_id","book_id")

    recallData
  }
}

object ALSRecall {
  def apply(data: DataFrame): ALSRecall = new ALSRecall(data)
}
