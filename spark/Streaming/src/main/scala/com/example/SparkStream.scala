package com.example

import com.example.model.EventModel
import com.example.utils.{Decode, ModelUtil}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.{Duration, StreamingContext}
import kafka.Kafka
import org.apache.spark.ml.classification.LogisticRegressionModel
import org.apache.spark.sql.SparkSession
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

    val modelUtil = ModelUtil(spark, sc)

    kafkaStream.map(record => (record.key, record.value))
        .foreachRDD {
          rdd => rdd.foreach {
            case (k, v) =>
              modelUtil.saveAction(Decode.handleData(v))
          }
        }



    ssc.start()
    ssc.awaitTermination()

  }
}
