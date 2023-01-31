package com.teapot.scala.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
 * 需求：计算TopN主播
 * 1：直接使用sparkSession中的load方式加载json数据
 * 2：对这两份数据注册临时表
 * 3：执行sql计算TopN主播
 * 4：使用foreach将结果打印到控制台
 * Created by xuwei
 */
object TopNAnchorClusterScala {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()

    //创建SparkSession对象，里面包含SparkContext和SqlContext
    val sparkSession = SparkSession.builder()
      .appName("LoadAndSaveOpScala")
      .config(conf)
      .getOrCreate()

    //1：直接使用sparkSession中的load方式加载json数据
    val videoInfoDf = sparkSession.read.json("hdfs://bigdata01:9000/video_info.log")
    val giftRecordDf = sparkSession.read.json("hdfs://bigdata01:9000/gift_record.log")

    //2：对这两份数据注册临时表
    videoInfoDf.createOrReplaceTempView("video_info")
    giftRecordDf.createOrReplaceTempView("gift_record")

    //3：执行sql计算TopN主播
    val sql = "select " +
      "t4.area, " +
      "concat_ws(',',collect_list(t4.topn)) as topn_list " +
      "from( " +
      "select " +
      "t3.area,concat(t3.uid,':',cast(t3.gold_sum_all as int)) as topn " +
      "from( " +
      "select " +
      "t2.uid,t2.area,t2.gold_sum_all,row_number() over (partition by area order by gold_sum_all desc) as num " +
      "from( " +
      "select " +
      "t1.uid,max(t1.area) as area,sum(t1.gold_sum) as gold_sum_all " +
      "from( " +
      "select " +
      "vi.uid,vi.vid,vi.area,gr.gold_sum " +
      "from " +
      "video_info as vi " +
      "join " +
      "(select " +
      "vid,sum(gold) as gold_sum " +
      "from " +
      "gift_record " +
      "group by vid " +
      ")as gr " +
      "on vi.vid = gr.vid " +
      ") as t1 " +
      "group by t1.uid " +
      ") as t2 " +
      ")as t3 " +
      "where t3.num <=3 " +
      ") as t4 " +
      "group by t4.area "
    val resDf = sparkSession.sql(sql)

    //4：使用foreach将结果打印到控制台
    resDf.rdd
      .map(row => row.getAs[String]("area") + "\t" + row.getAs[String]("topn_list"))
      .saveAsTextFile("hdfs://bigdata01:9000/out-topn")

    sparkSession.stop()
  }

}
