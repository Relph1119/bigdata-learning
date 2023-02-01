package com.teapot.scala.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
 * 需求：使用sql操作DataFrame
 * Created by xuwei
 */
object DataFrameSqlScala {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local")

    //创建SparkSession对象，里面包含SparkContext和SqlContext
    val sparkSession = SparkSession.builder()
      .appName("DataFrameSqlScala")
      .config(conf)
      .getOrCreate()

    val stuPath = getClass.getClassLoader.getResource("data/student.json").getPath
    val stuDf = sparkSession.read.json(stuPath)

    //将DataFrame注册为一个临时表
    stuDf.createOrReplaceTempView("student")

    //使用sql查询临时表中的数据
    sparkSession.sql("select age,count(*) as num from student group by age")
      .show()

    sparkSession.stop()
  }

}
