package com.example.utils

import java.text.SimpleDateFormat
import java.util.Date


import org.apache.spark.mllib.linalg.{Vector, VectorUDT, Vectors}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.functions._

import scala.collection.mutable

class ModelUtil(spark: SparkSession) {

  def getFeature(): DataFrame = {


    val format = new SimpleDateFormat("yyyy-MM-dd")
    val date = format.format(new Date())

    // val modelPath = "/model/als_model/" + date
    val modelPath = "/model/als_model/2020-05-31"
    val itemFactors = spark.read.parquet(modelPath + "/itemFactors")
      .withColumnRenamed("id", "book_id")
      .withColumnRenamed("features", "book_features")
    val userFactors = spark.read.parquet(modelPath + "/userFactors")
      .withColumnRenamed("id", "user_id")
      .withColumnRenamed("features", "user_features")

    val getLabel = udf(f = (value: Double) => {
      if (value - 0 > 0.00001) 1
      else 0
    })


    val actions = spark.sql(
      "select user_id, book_id, pv_score + click_score as clicked, score from book_recommend.user_behavior_score"
    )

    import spark.implicits._

    val toVector = udf((value: Array[Float]) => {

      Vectors.dense(value.map(_.toDouble))
    })

    val cancatFeatures = udf((s: Double, f1: Array[Float], f2: Array[Float]) => {
      val data = mutable.ArrayBuffer[Double](s)
      for (i <- f1.indices) {
        data :+ f1(i).toDouble
      }
      for (i <- f2.indices) {
        data :+ f2(i).toDouble
      }

      Vectors.dense(data.toArray)
    })



    val data = userFactors.crossJoin(itemFactors)
      .join(actions, Seq[String]("user_id", "book_id")).na.fill(0.0)
      .withColumn("click", getLabel(col("clicked")))
      .select(col("click"), col("score"), col("user_features"), col("book_features"))
//        .withColumn("features",
//          cancatFeatures(col("score"), col("user_features"), col("book_features")))
//        .drop("user_features", "book_features","score")
        .withColumn("user_features", toVector(col("user_features")))
        .withColumn("book_features", toVector(col("book_features")))

      data.printSchema()

      println(Vector.isInstanceOf[VectorUDT])

    data
  }

  def saveResult(recommendData: DataFrame): Unit = {
    import java.util.Properties

    val prop = new Properties()
    prop.put("user", "book_recommend")
    prop.put("password", "dawangba1A")
    prop.put("driver", "com.mysql.cj.jdbc.Driver")


    recommendData.write.mode("append")
      .jdbc(
        "jdbc:mysql://rm-bp14h269ydw6qq5h50o.mysql.rds.aliyuncs.com:3306/book_recommend?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai",
        "users_recommend",
        prop
      )
  }
}

object ModelUtil {
  def apply(spark: SparkSession): ModelUtil = new ModelUtil(spark)
}
