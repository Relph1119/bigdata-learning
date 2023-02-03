package com.imooc.spark

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * 需求：通过socket模拟产生数据，实时计算数据中单词出现的次数
 *
 * 启动Socket：nc -l 9001
 * Created by xuwei
 */
object StreamWordCountScala {
  def main(args: Array[String]): Unit = {
    //创建SparkConf配置对象
    val conf = new SparkConf()
      //注意：此处的local[2]表示启动2个进程，一个进程负责读取数据源的数据，一个进程负责处理数据
      .setMaster("local[2]")
      .setAppName("StreamWordCountScala")

    //创建StreamingContext，指定数据处理间隔为5秒
    val ssc = new StreamingContext(conf, Seconds(5))

    //通过socket获取实时产生的数据
    val linesRDD = ssc.socketTextStream("bigdata01", 9001)

    //对接收到的数据使用空格进行切割，转换成单个单词
    val wordsRDD = linesRDD.flatMap(_.split(" "))

    //把每个单词转换成tuple2的形式
    val tupRDD = wordsRDD.map((_, 1))

    //执行reduceByKey操作
    val wordCountRDD = tupRDD.reduceByKey(_ + _)

    //将结果数据打印到控制台
    wordCountRDD.print()

    //启动任务
    ssc.start()
    //等待任务停止
    ssc.awaitTermination()
  }
}
