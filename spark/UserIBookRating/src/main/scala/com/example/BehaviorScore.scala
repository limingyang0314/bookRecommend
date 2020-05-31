package com.example


import org.apache.spark.sql.functions.{col, udf}
import org.apache.spark.sql.{DataFrame, SparkSession}


class BehaviorScore(spark: SparkSession) {

  def generateBehaviorScore(year: Int, month: Int, day: Int): Unit = {

    val actions = List("pv-5", "pv", "want", "cart", "buy", "click", "review")

    val weights = List(0.2, 0.2, 0.94, 1.7, 2.4, 0.18, 0.58)

    val scores = List(1, 2, 3, 4, 5, 1, 2)


    var preTable: DataFrame = null

    for (i <- actions.indices) {
      val w = weights(i)
      val s = scores(i)
      val a = actions(i)
      var name = actions(i)

      spark.udf.register("interval",
        (d: Int) => {
          // 1 / (1 + math.log(day - d))
          1 / (1 + math.log(day - d + 1))
        }
      )
      if (i == 0) {
        name = "pv_5"
      }

      val viewScore = spark.sql(
        "select user_id, book_id, " +
          "count(*) * " + s + " * " + w  + " * interval(" + day + ") " +
          "as " + name  + "_score " +
          "from book_recommend.user_behavior " +
          "where action = '" + a + "' " +
          // "and year ='" + year + "' " +
          // "and month ='"+ month + "' " +
          // "and day ='"+ day + "' " +
          "group by user_id, book_id")

      if (preTable != null) {
        if (i == 1) {
          preTable = preTable.join(viewScore, Seq[String]("user_id", "book_id"), "full")
              .na.fill(0.0)
              .select(col("user_id"), col("book_id"),
                (col("pv_score") + col("pv_5_score")) as "pv_score")
        } else {
          preTable = preTable.join(viewScore, Seq[String]("user_id", "book_id"), "full")
            .na.fill(0.0)
        }
      } else {
        preTable = viewScore
      }
    }

    val jdbcDF = spark.read.format("jdbc")
      .option("url", "jdbc:mysql://rm-bp14h269ydw6qq5h50o.mysql.rds.aliyuncs.com:3306/book_recommend?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai")
      .option("driver", "com.mysql.cj.jdbc.Driver")
      .option("dbtable", "ratings")
      .option("user", "book_recommend")
      .option("password", "dawangba1A")
      .load()

    val finalTable = preTable.join(jdbcDF, Seq[String]("user_id", "book_id"), "full")
        .na.fill(0.0)
        .select(col("user_id"), col("book_id"), col("pv_score"),
          col("want_score"), col("cart_score"), col("buy_score"), col("click_score"),
          col("review_score"),
          (col("pv_score") +  col("want_score") + col("cart_score")
            + col("buy_score") + col("click_score")
            + col("review_score") + col("rating")) as "score")

    finalTable.createOrReplaceTempView("score_table")

      spark.sql("insert overwrite table " +
        "book_recommend.user_behavior_score " +
        "select * , " +
        year +" as year, " +
        month +" as month, " +
        day +" as day " +
        "from score_table"
      )

      spark.sql("insert overwrite table " +
        "book_recommend.user_features " +
        "select user_id, score " +
        "from score_table"
      )
    }

}


object BehaviorScore {
  def apply(spark: SparkSession): BehaviorScore = new BehaviorScore(spark)
}