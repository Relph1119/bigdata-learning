package com.imooc.spark

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * Spark 消费Kafka中的数据
 *
 * 执行命令向Kafka中写入数据
 * cd /data/soft/kafka_2.12-2.4.1
 * bin/kafka-console-producer.sh --broker-list localhost:9092 --topic t1
 *
 * Created by xuwei
 */
object StreamKafkaScala {
  def main(args: Array[String]): Unit = {
    //创建StreamingContext
    val conf = new SparkConf().setMaster("local[2]").setAppName("StreamKafkaScala")
    val ssc = new StreamingContext(conf, Seconds(5))

    //指定Kafka的配置信息
    val kafkaParams = Map[String, Object](
      //kafka的broker地址信息
      "bootstrap.servers" -> "bigdata01:9092",
      //key的序列化类型
      "key.deserializer" -> classOf[StringDeserializer],
      //value的序列化类型
      "value.deserializer" -> classOf[StringDeserializer],
      //消费者组id
      "group.id" -> "con_2",
      //消费策略
      "auto.offset.reset" -> "latest",
      //自动提交offset
      "enable.auto.commit" -> (true: java.lang.Boolean)
    )
    //指定要读取的topic的名称
    val topics = Array("t1")

    //获取消费kafka的数据流
    val kafkaDStream = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topics, kafkaParams)
    )

    //处理数据
    kafkaDStream.map(record => (record.key(), record.value()))
      //将数据打印到控制台
      .print()

    //启动任务
    ssc.start()
    //等待任务停止
    ssc.awaitTermination()
  }

}
