package com.example.utils

import java.security.MessageDigest

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.{Put, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapred.JobConf
import org.apache.hadoop.mapreduce.Job
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

class HBaseUtil(spark: SparkSession) extends Serializable {

  private val hBaseConfig = HBaseConfiguration.create()
  private val sc = spark.sparkContext

  def getData(tableName: String,
              cf: String,
              column: String): DataFrame = {

    hBaseConfig.set(TableInputFormat.INPUT_TABLE, tableName)
    val hBaseRDD: RDD[(ImmutableBytesWritable, Result)]
    = sc.newAPIHadoopRDD(hBaseConfig,
      classOf[TableInputFormat],
      classOf[ImmutableBytesWritable],
      classOf[Result])

    import spark.implicits._

    val rs = hBaseRDD.map(_._2)
      .map(r => {
        (r.getValue(
          Bytes.toBytes(cf),
          Bytes.toBytes(column)
        ))
      })
      .toDF("value")
    rs
  }


  def putData(tableName: String,
              data: DataFrame,
              cf: String,
              column: String
             ) : Unit = {

     val jobConf = new JobConf(hBaseConfig, this.getClass)
     jobConf.setOutputFormat(classOf[TableOutputFormat])
     jobConf.set(TableOutputFormat.OUTPUT_TABLE, tableName)

    // 新API
//     val jobConf = new JobConf(hBaseConfig, this.getClass)
//     jobConf.set(TableOutputFormat.OUTPUT_TABLE, tableName)
//     val job = Job.getInstance(jobConf)
//     job.setOutputKeyClass(classOf[ImmutableBytesWritable])
//     job.setOutputValueClass(classOf[Result])
//     job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]] )

    val _data = data.rdd.map(x => {
      val uid = x.getInt(0)
      val itemList = x.get(1)

      val rowKey = rowKeyHash(uid.toString)
      val put = new Put(Bytes.toBytes(rowKey))
      put.addColumn(Bytes.toBytes(cf),
        Bytes.toBytes(column),
        Bytes.toBytes(itemList.toString))
      (new ImmutableBytesWritable, put)

    })

    _data.saveAsHadoopDataset(jobConf)

    // _data.saveAsNewAPIHadoopDataset(job.getConfiguration)
  }

  // rowKey hash
  def rowKeyHash(key: String): String = {
    var md5: MessageDigest = null
    try {
      md5 = MessageDigest.getInstance("MD5")
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }

    val str = System.currentTimeMillis() + ":" + key
    val encode = md5.digest(str.getBytes())
    encode.map("%02x".format(_)).mkString
  }

}

object HBaseUtil{
  def apply(spark: SparkSession): HBaseUtil = new HBaseUtil(spark)
}