package com.teapot.scala.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}

/**
 * 需求：load和save的使用
 * Created by xuwei
 */
object LoadAndSaveOpScala {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local")

    //创建SparkSession对象，里面包含SparkContext和SqlContext
    val sparkSession = SparkSession.builder()
      .appName("LoadAndSaveOpScala")
      .config(conf)
      .getOrCreate()

    //读取数据
    val stuDf = sparkSession.read.format("json").load("D:\\student.json")

    //保存数据
    stuDf.select("name","age")
      .write
      .format("csv")
      .mode(SaveMode.Append)//追加
      .save("hdfs://bigdata01:9000/out-save001")

    sparkSession.stop()
  }

}
