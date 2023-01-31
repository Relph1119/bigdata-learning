package com.teapot.scala

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 需求：测试内存占用情况
 * Created by xuwei
 */
object TestMemoryScala {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("CheckpointOpScala")
      .setMaster("local")
    val sc = new SparkContext(conf)

    val dataRDD = sc.textFile("hdfs://bigdata01:9000/hello_10000000.dat").cache()
    val count = dataRDD.count()
    println(count)
    //while循环是为了保证程序不结束，方便在本地查看4040页面中的storage信息
    while (true) {
      ;
    }
  }

}
