package com.teapot.scala

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer

/**
 * 需求：mapPartitons的使用
 * Created by xuwei
 */
object MapPartitionsOpScala {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("MapPartitionsOpScala")
      .setMaster("local")
    val sc = new SparkContext(conf)

    //设置分区数量为2
    val dataRDD = sc.parallelize(Array(1, 2, 3, 4, 5), 2)

    //map算子一次处理一条数据
    /*val sum = dataRDD.map(item=>{
      println("==============")
      item * 2
    }).reduce( _ + _)*/

    //mapPartitions算子一次处理一个分区的数据
    val sum = dataRDD.mapPartitions(it => {
      //建议针对初始化链接之类的操作，使用mapPartitions，放在mapPartitions内部
      //例如：创建数据库链接，使用mapPartitions可以减少链接创建的次数，提高性能
      //注意：创建数据库链接的代码建议放在次数，不要放在Driver端或者it.foreach内部
      //数据库链接放在Driver端会导致链接无法序列化，无法传递到对应的task中执行，所以算子在执行的时候会报错
      //数据库链接放在it.foreach()内部还是会创建多个链接，和使用map算子的效果是一样的
      println("==================")
      val result = new ArrayBuffer[Int]()
      //这个foreach是调用的scala里面的函数
      it.foreach(item => {
        result.+=(item * 2)
      })
      //关闭数据库链接
      result.toIterator
    }).reduce(_ + _)


    println("sum:" + sum)

    sc.stop()
  }

}
