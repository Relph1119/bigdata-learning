package com.teapot.scala.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

/**
 * 需求：使用编程方式实现RDD转换为DataFrame
 * Created by xuwei
 */
object RddToDataFrameByProgramScala {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local")

    //创建SparkSession对象，里面包含SparkContext和SqlContext
    val sparkSession = SparkSession.builder()
      .appName("RddToDataFrameByReflectScala")
      .config(conf)
      .getOrCreate()

    //获取SparkContext
    val sc = sparkSession.sparkContext

    val dataRDD = sc.parallelize(Array(("jack", 18), ("tom", 20), ("jessica", 30)))

    //组装rowRDD
    val rowRDD = dataRDD.map(tup => Row(tup._1, tup._2))
    //指定元数据信息【这个元数据信息就可以动态从外部获取了，比较灵活】
    val schema = StructType(Array(
      StructField("name", StringType, nullable = true),
      StructField("age", IntegerType, nullable = true)
    ))

    //组装DataFrame
    val stuDf = sparkSession.createDataFrame(rowRDD, schema)

    //下面就可以通过DataFrame的方式操作dataRDD中的数据了
    stuDf.createOrReplaceTempView("student")

    //执行sql查询
    val resDf = sparkSession.sql("select name,age from student where age > 18")

    //将DataFrame转化为RDD
    val resRDD = resDf.rdd
    resRDD.map(row => (row(0).toString, row(1).toString.toInt))
      .collect()
      .foreach(println(_))

    sparkSession.stop()
  }

}
