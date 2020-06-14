package com.example.utils


import org.apache.spark.sql.functions._
import com.example.model.FTRL
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.ml.classification.LogisticRegressionModel
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.{DataFrame, DataFrameReader, Dataset, SparkSession}

import scala.collection.mutable

case class UserBehaviorDO(uuid: String, user_id: Long, book_id: Long, tagIds: String, action: String, year: Int, month: Int, day: Int, hour: Int)


class ModelUtil(spark: SparkSession) extends Serializable {

  def getJdbcDF: DataFrameReader ={
    spark.read.format("jdbc")
      .option("url", "jdbc:mysql://rm-bp14h269ydw6qq5h50o.mysql.rds.aliyuncs.com:3306/book_recommend?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai")
      .option("driver", "com.mysql.cj.jdbc.Driver")
      .option("user", "book_recommend")
      .option("password", "dawangba1A")
  }


  def saveAction(data: DataFrame, factorsB: Broadcast[DataFrame], userRecallB: Broadcast[DataFrame],
                 bookRecallB: Broadcast[DataFrame], bookRatingB: Broadcast[DataFrame], booksB: Broadcast[DataFrame]): Unit = {


    val factors = factorsB.value
    val userRecall = userRecallB.value
      .withColumnRenamed("books", "books_user")
    val bookRecall = bookRecallB.value
      .withColumnRenamed("books", "books_book")
    val bookRating = bookRatingB.value
    val books = booksB.value
    recommendUser(data, factors, userRecall, bookRecall, bookRating, books)
    data.write.mode("overwrite").insertInto("book_recommend.online_action")
  }

  def writeToDB(res: DataFrame, userRecall: DataFrame): Unit = {
    val toString = udf((values: Seq[Any]) => {
      values.mkString(",")
    })

    val data = res
      .withColumn("books", toString(col("books")))

    val ids = mutable.Set[Long]()
    data.rdd.collect().foreach(row => {
      val uid = row.getAs[Long]("user_id")
      ids.add(uid)
    })

    val returnData = userRecall.filter(!col("user_id").isin(ids.toSeq : _*))
      .union(data)

     import java.util.Properties
     val prop = new Properties()
     prop.put("user", "book_recommend")
     prop.put("password", "dawangba1A")
     prop.put("driver", "com.mysql.cj.jdbc.Driver")
    returnData.write.mode("overwrite")
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
    val modelPath = "/model/lr"
    val lrModel = LogisticRegressionModel.load(modelPath)
    val weight = lrModel.coefficientMatrix.toArray
    val ftrl = FTRL(weight)

    val len = weight.length

    val recall = getVectorFeature(list)
    var count = -1
    val fitData = recall.select("features").rdd
        .map(row => {
          count += 1
          (count % len, Vectors.dense(row.getAs[Seq[Double]](0).toArray))
        })

    ftrl.fit(fitData, 1)

    val pData = recall.select("user_id","book_id", "features").rdd
      .map(row => {
        val vector = Vectors.dense(row.getAs[org.apache.spark.ml.linalg.DenseVector]("features").toArray)
        val p = ftrl.predict(vector)
        val uid = row.getAs[Long](0)
        val bid = row.getAs[Long](1)
      uid -> (bid, p)
    })

    val finalData = pData.groupByKey().map(x => {
      val uid = x._1.toLong
      val books = x._2.toArray.sortBy(-_._2).take(40).map(_._1)
      (uid, books)
    })

    import spark.implicits._
    finalData.toDF("user_id", "books")
  }

  def recommendUser(data: DataFrame, factors: DataFrame, userRecall: DataFrame,
                    bookRecall: DataFrame, bookRating: DataFrame, books: DataFrame): Unit = {

    val toList = udf((value: String) => {
      if (value == null || value.isEmpty) {
        Array[Long](0)
      } else {
        value.split(",").filter(x => !x.isEmpty).map(_.trim.toLong)
      }
    })

    val deduplicate = udf((v1: Seq[Long], v2: Seq[Long]) => {
      val set = mutable.Set[Long]()

      for (i <- v1.indices) {
        set.add(v1(i))
      }
      for (i <- v2.indices) {
        set.add(v2(i))

      }
      set.toArray
    })

    val toVector = udf((value: Seq[Float]) => {
      Vectors.dense(value.map(_.toDouble).toArray)
    })

    val list = data.join(bookRecall, "book_id")
      .select("user_id", "book_id", "books_book")
      .join(userRecall, "user_id")
      .select("user_id", "books_user", "books_book")
      .withColumn("book_id", explode(deduplicate(toList(col("books_book")), toList(col("books_user")))))
      .join(bookRating, Seq[String]("book_id", "user_id")).join(factors, Seq[String]("book_id", "user_id"))
      .orderBy(col("rating").desc).limit(50)
      .select("user_id", "book_id", "rating", "book_features", "user_features")
      .withColumnRenamed("rating", "score")
      .withColumn("book_features", toVector(col("book_features")))
      .withColumn("user_features", toVector(col("user_features")))


    val res = lrRank(list)

    val tagData = tagsRecall(res, books)

    val realData = res.join(tagData, Seq[String]("user_id"), "full")

    writeToDB(realData, userRecall.withColumnRenamed("books_user", "books"))
  }

  def tagsRecall(data: DataFrame, books: DataFrame): DataFrame = {

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
