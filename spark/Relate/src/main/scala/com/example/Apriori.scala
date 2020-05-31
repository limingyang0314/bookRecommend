package com.example


import com.example.utils.ModelUtil
import org.apache.spark.sql.SparkSession

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.control.Breaks._


object Apriori {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext
    sc.setLogLevel("WARN")

    val modelUtil = ModelUtil(spark)
    val books = modelUtil.getBooks

    /**
     * Set(1, 3, 4)
     * Set(2, 3, 5)
     * Set(1, 2, 3, 5)
     * Set(2, 5)
     */

    val minSupport = 0.4

    // iter number
    val k = 3

    val records = books.count.toDouble

    val f_1 = books.flatMap(x => x)
      .map(x => (x, 1))
      .reduceByKey(_ + _)
      .filter(x => x._2.toDouble / records >= minSupport)
      .map{
        x =>
          val item = mutable.Set(x._1)
          val count = x._2
          (item, count)
      }.collect().map(_._1)

    var pre_f = f_1
    pre_f.foreach(println)

    /**
     *
     * Set(3)
     * Set(5)
     *
     */

    for(i <- 2 to k){
      println(s"the $i th iter")
      val candidate = candidateF(f_1, i, pre_f)
      val candidateBroadcast = sc.broadcast(candidate)

      val _f = books.flatMap(line =>
        candidateS(candidateBroadcast.value, mutable.Set(line).flatten)) // ((2,3),1)
        .reduceByKey(_ + _)
        .filter(x => x._2.toDouble / records >= minSupport)
        .collect().map(_._1)

      println("result:")
      _f.foreach(println(_))
      pre_f = _f
    }

    modelUtil.saveRecommend(pre_f.map(_.toSeq).toList, "append")
  }

  def candidateF(f_1: Array[mutable.Set[Long]],
                 k: Int,
                 pre_f: Array[mutable.Set[Long]]): Array[mutable.Set[Long]] = {

    val arraySet = f_1.combinations(k).map {
      x =>
        var set = mutable.Set[Long]()
        for(n <- x.indices){
          if((n + 1) < x.length) {
            val _x = x(n)
            set = set ++ (_x ++ (x(n + 1)))
          }
        }
        set
    }.filter(line => verify(line, pre_f, k))

    arraySet.toArray
  }

  def verify(line: mutable.Set[Long],
             pre_f: Array[mutable.Set[Long]],
             k: Int): Boolean = {

    var boolean = false
    val iter = line.subsets(k - 1)
    breakable {
      while (iter.hasNext) {
        if (pre_f.contains(iter.next())) {
          boolean = true
          break()
        }
      }
    }
    boolean
  }

  def candidateS(candidate: Array[mutable.Set[Long]],
                 data: mutable.Set[Long]): Array[(mutable.Set[Long], Long)] = {

    var tmp = new ArrayBuffer[(mutable.Set[Long], Long)]()
    for (c <- candidate) {
      if (c.subsetOf(data)) {
        tmp += ((c, 1))
      }
    }
    tmp.toArray
  }

}
