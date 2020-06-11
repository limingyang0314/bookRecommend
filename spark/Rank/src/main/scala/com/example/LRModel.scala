package com.example

import org.apache.spark.ml.classification.{LogisticRegression, LogisticRegressionModel}
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.Vector
import org.apache.spark.sql.functions.{col, explode, udf}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

class LRModel(spark: SparkSession) {

  var trainingData: DataFrame = _

  def getVectorFeature(training: DataFrame): Dataset[_] = {
    val vector = new VectorAssembler()
      .setInputCols(Array("score", "user_features", "book_features"))
      .setOutputCol("features")

    vector.transform(training).drop("score", "user_features", "book_features")
  }

  def getModel(training: DataFrame): LogisticRegressionModel = {
    trainingData = training

    val trainFeature = getVectorFeature(training)

    val lr = new LogisticRegression()
      .setLabelCol("click")
      .setFeaturesCol("features")
      .setMaxIter(500)


    val lrModel = lr.fit(trainFeature)

    lrModel.write.overwrite().save("/model/lr")

    lrModel

  }

  def getData: Dataset[_] = {
    val toList = udf((value: String) => {
      value.split(",").filter(!_.isEmpty).map(_.trim.toLong)
    })

    val recall = spark.sql("select * from book_recommend.users_recall")
      .withColumn("book_id",
        explode(toList(col("books"))))
      .join(trainingData, Seq[String]("user_id", "book_id"), "right")

    getVectorFeature(recall)


  }

  def getRank(lr: LogisticRegressionModel): DataFrame = {

    val recall = getData
    val _recall = lr.transform(recall)
                  .rdd.map(row => row.getAs[Integer](0) -> (row.getAs[Integer](1), row.getAs[Vector](5)))
                  .groupByKey().map(m => {
                      val user_id = m._1.toLong
                      val top40 = m._2.toArray.sortBy(-_._2(0)).take(40).map(_._1.toLong)
                      (user_id, top40)
                    })
    import spark.implicits._
    _recall.toDF("user_id", "books")

  }
}

object LRModel {
  def apply(spark: SparkSession): LRModel = new LRModel(spark)
}