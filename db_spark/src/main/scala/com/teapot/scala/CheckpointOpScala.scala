package com.teapot.scala

import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 需求：checkpoint的使用
 * Created by xuwei
 */
object CheckpointOpScala {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("CheckpointOpScala")
    //.setMaster("local")
    val sc = new SparkContext(conf)
    if (args.length == 0) {
      System.exit(100)
    }

    val outputPath = args(0)

    //1：设置checkpoint目录
    sc.setCheckpointDir("hdfs://bigdata01:9000/chk001")

    val dataRDD = sc.textFile("hdfs://bigdata01:9000/hello_10000000.dat")
      .persist(StorageLevel.DISK_ONLY) //执行持久化

    //2：对rdd执行checkpoint操作
    dataRDD.checkpoint()

    dataRDD.flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)
      .saveAsTextFile(outputPath)

    sc.stop()

  }

}
