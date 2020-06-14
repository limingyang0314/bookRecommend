package com.example

import java.util

import com.example.model.EventModel
import com.example.utils.{Decode, ModelUtil, UserBehaviorDO}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.{Duration, StreamingContext}
import kafka.Kafka
import org.apache.spark.ml.classification.LogisticRegressionModel
import org.apache.spark.sql.{DataFrameReader, SparkSession}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

object SparkStream {

  def main(args: Array[String]) {
    val spark = SparkSession.builder()
      .appName("Sparktream")
      //.master("yarn-cluster")
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()

    val sc = spark.sparkContext
    sc.setLogLevel("ERROR")

    val topics = "streaming"
    val zkQuorum = "localhost:9092"
    val groupId = "test-consumer-group"

    val kafkaParam = Map (
      "bootstrap.servers" -> zkQuorum,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> groupId,
      "auto.offset.reset" -> "latest"
    )

    val ssc = new StreamingContext(sc, Seconds(5))
    val kafkaStream = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](Array(topics), kafkaParam)
    )


    val modelUtil = ModelUtil(spark)
    // val modelPath = "/model/als_model/" + date
    val modelPath = "/model/als_model/2020-05-31"
    val itemFactors = spark.read.parquet(modelPath + "/itemFactors")
      .withColumnRenamed("id", "book_id")
      .withColumnRenamed("features", "book_features")

    val userFactors = spark.read.parquet(modelPath + "/userFactors")
      .withColumnRenamed("id", "user_id")
      .withColumnRenamed("features", "user_features")

    val factors = sc.broadcast(itemFactors.crossJoin(userFactors))

    val userRecall = sc.broadcast(modelUtil.getJdbcDF.option("dbtable", "users_recommend")
      .load())

    val bookRecall = sc.broadcast(modelUtil.getJdbcDF.option("dbtable", "books_recommend")
      .load())

    val bookRating = sc.broadcast(spark.sql("select * from book_recommend.user_book_rating"))

    val books = sc.broadcast(modelUtil.getJdbcDF.option("dbtable", "books")
      .load())

    kafkaStream.map(record => (record.key, record.value))
      .foreachRDD {
        rdd => {
          if (rdd != null && !rdd.isEmpty()) {
            val newEDD = rdd.flatMap {
              case (k, v) =>
                var value = List[UserBehaviorDO]()
                val list = Decode.handleData(v)
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
                value
            }
            import spark.implicits._
            val data = newEDD.toDF()
            modelUtil.saveAction(data, factors, userRecall, bookRecall, bookRating, books)
          }
        }
      }


    ssc.start()
    ssc.awaitTermination()

  }
}
