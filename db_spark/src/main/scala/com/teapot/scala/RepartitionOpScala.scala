package com.teapot.scala

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 需求：repartition的使用
 * Created by xuwei
 */
object RepartitionOpScala {
  def main(args: Array[String]): Unit = {
    // 设置上传权限
    sys.props += (("HADOOP_USER_NAME", "root"))

    val conf = new SparkConf()
    conf.setAppName("RepartitionOpScala")
      .setMaster("local")
    val sc = new SparkContext(conf)

    //设置分区数量为2
    val dataRDD = sc.parallelize(Array(1, 2, 3, 4, 5), 2)

    //重新设置RDD的分区数量为3，这个操作会产生shuffle
    //也可以解决RDD中数据倾斜的问题
    dataRDD.repartition(3)
      .foreachPartition(it => {
        println("=========")
        it.foreach(println(_))
      })

    //通过repartition可以控制输出数据产生的文件个数
    dataRDD.saveAsTextFile("hdfs://bigdata01:9000/rep-001")
    dataRDD.repartition(1).saveAsTextFile("hdfs://bigdata01:9000/rep-002")

    sc.stop()
  }

}
