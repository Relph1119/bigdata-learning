package com.teapot.scala

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 需求：RDD持久化
 * Created by xuwei
 */
object PersistRddScala {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("PersistRddScala")
      .setMaster("local")
    val sc = new SparkContext(conf)

    // 注意cache的用法和位置
    // cache默认是基于内存的持久化
    // cache()=persist()=persist(StorageLevel.MEMORY_ONLY)
    val dataRDD = sc.textFile("D:\\hello_10000000.dat").cache()
    var start_time = System.currentTimeMillis()
    var count = dataRDD.count()
    println(count)
    var end_time = System.currentTimeMillis()
    println("第一次耗时：" + (end_time - start_time))

    start_time = System.currentTimeMillis()
    count = dataRDD.count()
    println(count)
    end_time = System.currentTimeMillis()
    println("第二次耗时：" + (end_time - start_time))

    sc.stop()
  }

}
