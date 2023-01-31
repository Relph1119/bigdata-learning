package com.teapot.scala

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 需求：单词计数
 * Created by xuwei
 */
object WordCountScala {

  def main(args: Array[String]): Unit = {
    //第一步：创建SparkContext
    val conf = new SparkConf()
    conf.setAppName("WordCountScala") //设置任务名称
    //      .setMaster("local") //local表示在本地执行
    val sc = new SparkContext(conf)

    //第二步：加载数据
    var path = getClass.getClassLoader.getResource("data/hello.txt").getPath
    if (args.length == 1) {
      path = args(0)
    }
    val linesRDD = sc.textFile(path)

    //第三步：对数据进行切割，把一行数据切分成一个一个的单词
    val wordsRDD = linesRDD.flatMap(_.split(" "))

    //第四步：迭代words，将每个word转换为(word,1)这种形式
    val pairRDD = wordsRDD.map((_, 1))

    //第五步：根据key(其实就是word)进行分组聚合统计
    val wordCountRDD = pairRDD.reduceByKey(_ + _)

    //第六步：将结果打印到控制台
    //注意：只有当任务执行到这一行代码的时候，任务才会真正开始执行计算
    //如果任务中没有这一行代码，前面的所有算子是不会执行的
    wordCountRDD.foreach(wordCount => println(wordCount._1 + "--" + wordCount._2))

    //第七步：停止SparkContext
    sc.stop()
  }
}
