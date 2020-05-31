package com.example

import com.example.utils.ModelUtil
import org.apache.spark.ml.feature.StandardScaler
import org.apache.spark.ml.fpm.FPGrowth
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

import scala.collection.mutable

object FPGrowth {
  def main(args: Array[String]): Unit = {


    val spark = SparkSession.builder()
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext
    sc.setLogLevel("WARN")

    val modelUtil = ModelUtil(spark)
    val books = modelUtil.getDF

    // import spark.implicits._
    // val books = modelUtil.getBooks.toDF("books")
    // books.show()

    val fpGrowth = new FPGrowth().setItemsCol("books")
      .setMinSupport(0.5) // 0.5
      .setMinConfidence(0.2) // 0.6

    val model = fpGrowth.fit(books)

    model.freqItemsets.show()

    val list = model.freqItemsets.select("items")
      .rdd
      .map(row => row.getAs[mutable.Seq[Long]](0))
      .filter(_.length >= 2)
      .collect().toList

    modelUtil.saveRecommend(list, "append")

  }

}
