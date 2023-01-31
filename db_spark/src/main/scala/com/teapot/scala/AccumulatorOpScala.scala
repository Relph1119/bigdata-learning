package com.teapot.scala

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 需求：使用累加变量
 * Created by xuwei
 */
object AccumulatorOpScala {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("AccumulatorOpScala")
      .setMaster("local")
    val sc = new SparkContext(conf)
    val dataRDD = sc.parallelize(Array(1, 2, 3, 4, 5))
    //这种写法是错误的，因为foreach代码是在worker节点上执行的
    //var total = 0 和 println(total) 是在Driver进程中执行的
    //所以无法实现累加操作
    //并且foreach算子可能会在多个task中执行，这样foreach内部实现的累加也不是最终全局累加的结果
    /*var total = 0
    dataRDD.foreach(num=>total += num)
    println(total)*/

    //所以此时想要实现累加操作就需要使用累加变量了
    //1：定义累加变量
    val sumAccumulator = sc.longAccumulator

    //2：使用累加变量
    dataRDD.foreach(num => sumAccumulator.add(num))

    //注意：只能在Driver进程中获取累加变量的结果
    println(sumAccumulator.value)


    sc.stop()
  }

}
