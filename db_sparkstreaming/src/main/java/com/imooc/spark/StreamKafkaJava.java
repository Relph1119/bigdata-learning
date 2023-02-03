package com.imooc.spark;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Spark 消费Kafka中的数据
 *
 * 执行命令向Kafka中写入数据
 * cd /data/soft/kafka_2.12-2.4.1
 * bin/kafka-console-producer.sh --broker-list localhost:9092 --topic t1
 *
 * Created by xuwei
 */
public class StreamKafkaJava {
    public static void main(String[] args) throws Exception{
        //创建StreamingContext，指定读取数据的时间间隔为5秒
        SparkConf conf = new SparkConf()
                .setMaster("local[2]")
                .setAppName("StreamKafkaJava");
        JavaStreamingContext ssc = new JavaStreamingContext(conf, Durations.seconds(5));

        //指定kafka的配置信息
        HashMap<String, Object> kafkaParams = new HashMap<String, Object>();
        kafkaParams.put("bootstrap.servers","bigdata01:9092");
        kafkaParams.put("key.deserializer", StringDeserializer.class.getName());
        kafkaParams.put("value.deserializer", StringDeserializer.class.getName());
        kafkaParams.put("group.id","con_2");
        kafkaParams.put("auto.offset.reset","latest");
        kafkaParams.put("enable.auto.commit",true);


        //指定要读取的topic名称
        ArrayList<String> topics = new ArrayList<String>();
        topics.add("t1");

        //获取消费kafka的数据流
        JavaInputDStream<ConsumerRecord<String, String>> kafkaStream = KafkaUtils.createDirectStream(
                ssc,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams)
        );

        //处理数据
        kafkaStream.map(new Function<ConsumerRecord<String, String>, Tuple2<String,String>>() {
            public Tuple2<String, String> call(ConsumerRecord<String, String> record)
                    throws Exception {
                return new Tuple2<String, String>(record.key(),record.value());
            }
        }).print();//将数据打印到控制台

        //启动任务
        ssc.start();
        //等待任务停止
        ssc.awaitTermination();
    }
}
