package com.teapot.scala

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 需求：通过文件创建RDD
 * Created by xuwei
 */
object CreateRddByFileScala {
  def main(args: Array[String]): Unit = {
    //创建SparkContext
    val conf = new SparkConf()
    conf.setAppName("CreateRddByFileScala")
      .setMaster("local")
    val sc = new SparkContext(conf)

    var path = "D:\\hello.txt"
    path = "hdfs://bigdata01:9000/test/hello.txt"
    //读取文件数据，可以在textFile中指定生成的RDD的分区数量
    val rdd = sc.textFile(path, 2)

    //获取每一行数据的长度，计算文件内数据的总长度
    val length = rdd.map(_.length).reduce(_ + _)

    println(length)

    sc.stop()
  }

}
