package com.teapot.scala.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
 * 需求：通过反射方式实现RDD转换为DataFrame
 * Created by xuwei
 */
object RddToDataFrameByReflectScala {
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

    val dataRDD = sc.parallelize(Array(("jack", 18), ("tom", 20), ("jessic", 30)))

    //基于反射直接将包含Student对象的dataRDD转换为dataFrame

    //需要导入隐式转换
    import sparkSession.implicits._
    val stuDf = dataRDD.map(tup => Student(tup._1, tup._2)).toDF()

    //下面就可以通过DataFrame的方式操作dataRDD中的数据了
    stuDf.createOrReplaceTempView("student")

    //执行sql查询
    val resDf = sparkSession.sql("select name,age from student where age > 18")

    //将DataFrame转化为RDD
    val resRDD = resDf.rdd
    //从row中取数据，封装成student，打印到控制台
    resRDD.map(row => Student(row(0).toString, row(1).toString.toInt))
      .collect()
      .foreach(println(_))

    //使用row的getAs方法，获取指定列名的值
    resRDD.map(row => Student(row.getAs[String]("name"), row.getAs[Int]("age")))
      .collect()
      .foreach(println(_))

    sparkSession.stop()
  }

}

//定义一个Student
case class Student(name: String, age: Int)