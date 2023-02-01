package com.teapot.scala

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 需求：设置并行度
 * 1：可以在textFile或者是parallelize方法的第二个参数中设置并行度【针对具体的RDD设置】
 * 2：或者通过spark.default.parallelism参数统一设置并行度【全局设置】
 * Created by xuwei
 */
object MoreParallelismScala {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("MoreParallelismScala")
//      .setMaster("local")
    //设置全局并行度
    //conf.set("spark.default.parallelism","5")

    val sc = new SparkContext(conf)

    val dataRDD = sc.parallelize(Array("hello", "you", "hello", "me", "hehe", "hello", "you", "hello", "me", "hehe"))
    dataRDD.map((_, 1))
      .reduceByKey(_ + _)
      .foreach(println(_))

    sc.stop()
  }

}
