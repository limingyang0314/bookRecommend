package com.example.utils

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.{DataFrame, DataFrameReader, Row, SparkSession}
import org.apache.spark.sql.functions._

import scala.collection.mutable

class ModelUtil(spark: SparkSession) extends Serializable {

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

    val toVector = udf((value: Seq[Float]) => {
      Vectors.dense(value.map(_.toDouble).toArray)
    })

    val data = userFactors.crossJoin(itemFactors)
      .join(actions, Seq[String]("user_id", "book_id")).na.fill(0.0)
      .withColumn("click", getLabel(col("clicked")))
      .select(
        col("user_id"), col("book_id"),
        col("click"), col("score"),
        col("user_features"),
        col("book_features"))
//        .withColumn("features",
//          cancatFeatures(col("score"), col("user_features"), col("book_features")))
//        .drop("user_features", "book_features","score")
        .withColumn("user_features", toVector(col("user_features")))
        .withColumn("book_features", toVector(col("book_features")))

    data
  }

  def saveResult(recommendData: DataFrame): Unit = {

    val toString = udf((values: Seq[Any]) => {
      values.mkString(",")
    })


    val tags = tagsRecall(recommendData)

    val data = recommendData
        .withColumn("books", toString(col("books")))
      .join(tags, Seq[String]("user_id"), "full")

    import java.util.Properties

    val prop = new Properties()
    prop.put("user", "book_recommend")
    prop.put("password", "dawangba1!A")
    prop.put("driver", "com.mysql.cj.jdbc.Driver")


    data.write.mode("append")
      .jdbc(
        "jdbc:mysql://bookrecommend.rwlb.rds.aliyuncs.com:3306/book_recommend?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai",
        "users_recommend",
        prop
      )
  }

  def getJdbcDF: DataFrameReader ={
    spark.read.format("jdbc")
      .option("url", "jdbc:mysql://bookrecommend.rwlb.rds.aliyuncs.com:3306/book_recommend?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai")
      .option("driver", "com.mysql.cj.jdbc.Driver")
      .option("user", "book_recommend")
      .option("password", "dawangba1!A")
  }

  def tagsRecall(data: DataFrame): DataFrame = {

    val toList = udf((value: String) => {
      if (value == null || value.isEmpty) {
        Array[Long](0)
      } else {
        value.split(",").filter(x => !x.isEmpty).map(_.trim.toLong)
      }
    })

    val books = getJdbcDF.option("dbtable", "books")
      .load()

    val rdd = data.withColumn("book_id", explode(col("books")))
      .join(books, Seq[String]("book_id"), "left")
      .select("user_id", "tag_ids").na.fill("", Array[String]("tag_ids"))
      .withColumn("tag_id", explode(toList(col("tag_ids"))))
        .drop("tag_ids")
      .rdd.map(row => row.getAs[Long](0) -> row.getAs[Long](1))
      .groupByKey().map(x => {
          val uid = x._1.toLong
          val map = mutable.Map[Long, Int]()

          def order(tuple2: (Long, Int)) = -tuple2._2
          val pq = mutable.PriorityQueue[(Long, Int)]()(Ordering.by(order))
          var output = Array[Long]()

          val items = x._2.toSeq
          for (i <- items.indices) {
              map.put(items(i), map.getOrElse(items(i), 0) + 1)
          }

          map.foreach(x => {
            pq.enqueue(x)
            if (pq.size > 10) {
              pq.dequeue()
            }
          })

          while (pq.nonEmpty) {
            output = pq.dequeue()._1 +: output
          }

          (uid, output.mkString(","))
        })
    import spark.implicits._
    val tagDF = rdd.toDF("user_id", "tags")

    tagDF
  }
}

object ModelUtil {
  def apply(spark: SparkSession): ModelUtil = new ModelUtil(spark)
}
