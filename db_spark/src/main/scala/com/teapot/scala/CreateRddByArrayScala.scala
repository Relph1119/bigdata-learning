package com.teapot.scala

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 需求：使用集合创建RDD
 * Created by xuwei
 */
object CreateRddByArrayScala {

  def main(args: Array[String]): Unit = {
    //创建SparkContext
    val conf = new SparkConf()
    conf.setAppName("CreateRddByArrayScala")
      .setMaster("local")
    val sc = new SparkContext(conf)

    //创建集合
    val arr = Array(1, 2, 3, 4, 5)
    //基于集合创建RDD
    val rdd = sc.parallelize(arr)
    //对集合中的数据求和
    val sum = rdd.reduce(_ + _)

    //注意：这行println代码是在driver进程中执行的
    println(sum)
  }

}
