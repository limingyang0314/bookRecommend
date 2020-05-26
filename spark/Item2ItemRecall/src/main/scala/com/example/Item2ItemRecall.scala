package com.example

import com.github.fommil.netlib.F2jBLAS
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._

import scala.collection.mutable

class Item2ItemRecall extends Serializable {

  def getCosSim(_itemFactors: DataFrame, spark: SparkSession): DataFrame={

    /**
     *  _itemFactors 表结构
     *
     *  itemid  特征值
     *  3       [0.42,0.23,0.42]
     *  4       [0.1,0.32,0.32]
     *
     */
    import spark.implicits._
    val itemFactors = _itemFactors.as[(Int, Array[Float])]
      .mapPartitions(_.grouped(4096))

    val itemCosinSim = itemFactors.crossJoin(itemFactors)
      .as[(Seq[(Int, Array[Float])], Seq[(Int, Array[Float])])]
      .flatMap {
        x =>
          x match {
            case (itF_1, itF_2) =>
              val size_1 = itF_1.size
              val size_2 = itF_2.size
              // 物品相似度矩阵
              val outPut = new Array[(Int, Int, Float)](size_1 * size_2)
              val pq = mutable.PriorityQueue[(Int, Float)]()
              val rank = size_2
              var i = 0
              val operator = new F2jBLAS
              itF_1.foreach {
                case (item1, item1F) =>
                  itF_2.foreach {
                    case (item2, item2F) =>
                      // 计算余弦相似度
                      val sim = cosinSim(
                        operator,
                        item1F,
                        item2F,
                        rank)
                      pq.enqueue((item2, sim))
                  }
                  pq.foreach {
                    case (item2, cosSim) =>
                      outPut(i) = (item1, item2, cosSim)
                      i += 1
                  }
                  pq.clear()

              }

              outPut.toSeq
          }
      }.toDF("book_id_1", "book_id_2", "sim")
    itemCosinSim
  }

  // 获取基于物品协同过滤的召回
  def getItem2ItemRecall(data: DataFrame,
                         itemCosSim: Broadcast[DataFrame],
                         spark: SparkSession): DataFrame= {

    val itemSim = itemCosSim.value

    /**
     * data 表结构
     * 用户id 物品id  打分
     * uid    itemid  rating
     *
     * itemSim 表结构
     * 物品id 1  物品id 2  物品1和物品2相似度
     * itemid_1  itemid_2   sim
     *
     *
     */

    //itemid_1 是用户未打分的物品
    //现在要推测的就是用户对itemid_1的兴趣度

    val interest = data.join(itemSim,
      data("book_id") === itemSim("book_id_2"))
      .where(col("book_id") =!= col("book_id_1"))
      .where(col("book_id_2") =!= col("book_id_1"))
      .select(
        data("user_id"),
        //        data("book_id"),
        itemSim("book_id_1"),
        //        itemSim("book_id_2"),
        (data("rating") * itemSim("sim")).as("interest")
      )


    // 这里是最耗时的地方
    // 表的数量特别的大，要注意OOM

    val recallData = interest.groupBy(col("user_id"),
      col("book_id_1"))
      .agg(sum(col("interest")))
      .withColumnRenamed("sum(interest)",
        "recom")
      .withColumnRenamed("book_id_1",
        "book_id")
      .select(col("uid"),
        col("book_id"),
        col("recom"))
      .orderBy(desc("recom"))

    recallData


  }

  def cosinSim(operator: F2jBLAS,
               itemF1: Array[Float],
               itemF2: Array[Float],
               rank: Int): Float = {

    val i1 = operator.sdot(rank, itemF1,1, itemF2, 1)
    val i2 = operator.snrm2(rank, itemF1,1)
    val i3 = operator.snrm2(rank, itemF2,1)

    i1 / i2 * i3

  }
}

object Item2ItemRecall{
  def apply(): Item2ItemRecall = new Item2ItemRecall
}