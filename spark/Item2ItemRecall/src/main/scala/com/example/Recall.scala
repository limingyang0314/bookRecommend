package com.example

import java.text.SimpleDateFormat
import java.util.Date

import com.example.utils.ModelUtil
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.{DataFrame, SparkSession}

object Recall {
  //column
  val colItem2Item = "item2item"

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("recall")
      .master("yarn-cluster")
      .enableHiveSupport()
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    /**
     *  通常这个任务运行时间是在凌晨0-3am
     *  执行的数据是前一天的数据
     *
     *  例如现在是周三凌晨运行这两个召回任务
     *  使用的样本数据是周二产生的用户行为数据
     *
     *  一般每个召回策略各自独立项目分开运行
     *
     */


    //数据处理
    val modelUtil = ModelUtil(spark)
    val data:DataFrame = modelUtil.getUserItemRating

    /**
     *
     * 获取上一个任务存储的ALS模型生成的物品特征向量
     */
    val format = new SimpleDateFormat("yyyy-MM-dd")
    val date = format.format(new Date())
    val path = "/model/als_model/"+date+"/itemFactors"

    val itemFactors = spark.read.parquet(path)


    //生成候选集
    //召回2：基于物品的协同过滤
    val item2Item = Item2ItemRecall()
    //获取物品的相似度矩阵 相似度采用余弦相似度
    /**
     * 耗时约30m
     *
     * */
    val itemCosSim = item2Item.getCosSim(itemFactors,spark)
    //广播相似度矩阵
    val itemCosSimBd:Broadcast[DataFrame] =
      spark.sparkContext.broadcast(itemCosSim)
    //获取推荐
    val item2ItemRecallData
    =  item2Item.getItem2ItemRecall(data,
      itemCosSimBd,spark)
    //存储候选集
    modelUtil.saveRecall(item2ItemRecallData,colItem2Item)


    spark.stop()
  }
}
