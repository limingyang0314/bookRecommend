package com.example

import org.apache.spark.ml.classification.{LogisticRegression, LogisticRegressionModel}
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.{DataFrame, SparkSession}

class LRModel(spark: SparkSession) {

  def getModel(training: DataFrame): LogisticRegressionModel = {


    val vector = new VectorAssembler()
      .setInputCols(Array("score", "user_features", "book_features"))
      .setOutputCol("features")

     val trainFeature = vector.transform(training)


    val lr = new LogisticRegression()
      .setLabelCol("click")
      .setFeaturesCol("features")
      .setMaxIter(500)

    val lrModel = lr.fit(trainFeature)

    lrModel.save("/models/lr/lr.obj")

    lrModel
  }

  def getData: DataFrame = {
    spark.sql("select * from book_recommend.users_recall")

  }

  def getRank(lr: LogisticRegressionModel): DataFrame = {

    val recall = getData
    val _recall = lr.transform(recall)
    _recall
  }
}

object LRModel {
  def apply(spark: SparkSession): LRModel = new LRModel(spark)
}