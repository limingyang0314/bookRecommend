package com.example.utils

import java.util

import org.apache.spark.sql.functions._
import com.example.model.EventModel
import org.apache.spark.SparkContext
import org.apache.spark.ml.classification.{LogisticRegression, LogisticRegressionModel}
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.{DataFrame, DataFrameReader, Dataset, Row, SparkSession}

import scala.collection.mutable

case class UserBehaviorDO(uuid: String, user_id: Long, book_id: Long, tagIds: String, action: String, year: Int, month: Int, day: Int, hour: Int)


class ModelUtil(spark: SparkSession, sparkContext: SparkContext) extends Serializable {

  def getJdbcDF: DataFrameReader ={
    spark.read.format("jdbc")
      .option("url", "jdbc:mysql://rm-bp14h269ydw6qq5h50o.mysql.rds.aliyuncs.com:3306/book_recommend?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai")
      .option("driver", "com.mysql.cj.jdbc.Driver")
      .option("user", "book_recommend")
      .option("password", "dawangba1A")
  }

  def saveAction(list: util.List[EventModel]): Unit = {
    var value = List[UserBehaviorDO]()
    for (i <- 0 until list.size()) {
      val x = list.get(i)
      value = value :+ UserBehaviorDO(
        x.getUuid,
        x.getUserId.toLong,
        x.getBookId.toLong,
        x.getTagIds,
        x.getAction,
        x.getYear.toInt,
        x.getMonth.toInt,
        x.getDay.toInt,
        x.getHour.toInt
      )
    }

    value.foreach(println)

    import spark.implicits._
    println(spark)
    println(sparkContext)

    if (sparkContext != null) {
      val rdd = sparkContext.makeRDD(value)

      val data = rdd.toDF("uuid", "user_id", "book_id", "tagIds", "action", "year", "month", "day", "hour")

      data.show()
    }


//  recommendUser(data)
//  data.write.mode("append").insertInto("online_action")
  }

  def writeToDB(res: DataFrame): Unit = {
     import java.util.Properties
     val prop = new Properties()
     prop.put("user", "book_recommend")
     prop.put("password", "dawangba1A")
     prop.put("driver", "com.mysql.cj.jdbc.Driver")
     res.write.mode("append")
       .jdbc(
         "jdbc:mysql://rm-bp14h269ydw6qq5h50o.mysql.rds.aliyuncs.com:3306/book_recommend?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai",
         "users_recommend",
         prop
       )
  }

  def getVectorFeature(training: DataFrame): Dataset[_] = {
    val vector = new VectorAssembler()
      .setInputCols(Array("score", "user_features", "book_features"))
      .setOutputCol("features")

    vector.transform(training).drop("score", "user_features", "book_features")
  }

  def lrRank(list: DataFrame): DataFrame = {
    val modelPath = "/model/lr/lr.obj"
    val lrModel = LogisticRegressionModel.load(modelPath)
//    val ftrl = FTRL(lrModel.coefficientMatrix.toArray)
//
    val recall = getVectorFeature(list)
//    var count = -1
//    val fitData = recall.select("features").rdd
//        .map(row => {
//          count += 1
//          (count, Vectors.dense(row.getAs[Seq[Double]](0).toArray))
//        })
//
//    ftrl.fit(fitData, 1)

    
    lrModel.transform(recall)
  }

  def recommendUser(data: DataFrame): Unit = {

    // val modelPath = "/model/als_model/" + date
    val modelPath = "/model/als_model/2020-05-31"
    val itemFactors = spark.read.parquet(modelPath + "/itemFactors")
      .withColumnRenamed("id", "book_id")
      .withColumnRenamed("features", "book_features")
    val userFactors = spark.read.parquet(modelPath + "/userFactors")
      .withColumnRenamed("id", "user_id")
      .withColumnRenamed("features", "user_features")

    val factors = itemFactors.crossJoin(userFactors)

    val userRecall = getJdbcDF.option("dbtable", "users_recommend")
        .load()

    val bookRecall = getJdbcDF.option("dbtable", "books_recommend")
      .load()

    val books = spark.sql("select * from book_recommend.user_book_rating")

    val list = data.join(bookRecall, Seq[String]("book_id")).join(userRecall, "user_id")
      .join(books, Seq[String]("book_id", "user_id")).join(factors, Seq[String]("book_id", "user_id"))
      .orderBy(col("score").desc).limit(50)
      .select("user_id", "book_id", "score", "book_features", "user_features")

    val res = lrRank(list)

    writeToDB(res)
  }


}

object ModelUtil {
  def apply(spark: SparkSession, sparkContext: SparkContext): ModelUtil = new ModelUtil(spark, sparkContext)
}
