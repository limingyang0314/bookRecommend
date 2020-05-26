package com.example.utils

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._

class ModelUtil(spark:SparkSession) {

  val rsRecall = "history_rs_recall"

  val cf = "recall"


  def getUserItemRating: DataFrame = {
    val data = spark.sql(
      "select * from dws.dws_user_item_rating")
    data

  }

  // 把召回策略生成的推荐候选集存储到HBase
  def saveRecall(recommend: DataFrame, cell: String): Unit = {

    /**
     * 目标生成格式
     *  uid     itemid
     *  3       [12,34,24,89,21]
     *  5       [19,78,67,21,12]
     */
    val recommList = recommend.groupBy(col("user_id"))
      .agg(collect_list("book_id"))
      .withColumnRenamed("collect_list(book_id)",
        "book_id")
      .select(col("uid"),
        col("book_id"))

    val HBase = new HBaseUtil(spark)
    HBase.putData(rsRecall, recommList, cf, cell)
  }


}

object ModelUtil{
  var spark: SparkSession = _
  def apply(spark: SparkSession): ModelUtil = new ModelUtil(spark)
}

