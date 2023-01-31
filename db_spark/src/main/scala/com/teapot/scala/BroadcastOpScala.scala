package com.teapot.scala

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 需求：使用广播变量
 * Created by xuwei
 */
object BroadcastOpScala {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("BroadcastOpScala")
      .setMaster("local")
    val sc = new SparkContext(conf)

    val dataRDD = sc.parallelize(Array(1, 2, 3, 4, 5))
    val variable = 2
    //dataRDD.map(_ * variable)
    //1：定义广播变量
    val variableBroadcast = sc.broadcast(variable)

    //2：使用广播变量，调用其value方法
    dataRDD.map(_ * variableBroadcast.value).foreach(println(_))

    sc.stop()
  }

}
