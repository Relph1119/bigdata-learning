package com.teapot.scala.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
 * 需求：DataFrame常见操作
 * Created by xuwei
 */
object DataFrameOpScala {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local")

    //创建SparkSession对象，里面包含SparkContext和SqlContext
    val sparkSession = SparkSession.builder()
      .appName("DataFrameOpScala")
      .config(conf)
      .getOrCreate()

    val stuDf = sparkSession.read.json("D:\\student.json")

    //打印schema信息
    stuDf.printSchema()

    //默认显示所有数据，可以通过参数控制显示多少条
    stuDf.show(2)

    //查询数据中的指定字段信息
    stuDf.select("name", "age").show()

    //在使用select的时候可以对数据做一些操作，需要添加隐式转换函数，否则语法报错
    import sparkSession.implicits._
    stuDf.select($"name", $"age" + 1).show()

    //对数据进行过滤，需要添加隐式转换函数，否则报错
    stuDf.filter($"age" > 18).show()
    //where底层调用的就是filter
    stuDf.where($"age" > 18).show()

    //对数据进行分组求和
    stuDf.groupBy("age").count().show()

    sparkSession.stop()
  }

}
