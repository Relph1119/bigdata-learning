package com.teapot.scala

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 需求：foreachPartition的使用
 * Created by xuwei
 */
object ForeachPartitionOpScala {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("ForeachPartitionOpScala")
      .setMaster("local")
    val sc = new SparkContext(conf)
    //设置分区数量为2
    val dataRDD = sc.parallelize(Array(1, 2, 3, 4, 5), 2)
    //foreachPartition：一次处理一个分区的数据，作用和mapPartitions类似
    //唯一的区是mapPartitions是transformation算子，foreachPartition是action算子
    dataRDD.foreachPartition(it => {
      //在此处获取数据库链接
      println("===============")
      it.foreach(item => {
        //在这里使用数据库链接
        println(item)
      })
      //关闭数据库链接
    })

    sc.stop()
  }

}
