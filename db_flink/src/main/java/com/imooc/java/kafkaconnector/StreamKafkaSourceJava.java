package com.imooc.java.kafkaconnector;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

/**
 * Flink从kafka中消费数据
 *
 * 启动HDFS、Zookeeper、Kafka
 * 创建t1的Topic：
 * cd /data/soft/kafka_2.12-2.4.1
 * bin/kafka-topics.sh --create --zookeeper localhost:2181 --partitions 1 --replication-factor 1 --topic t1
 * 启动生产者：
 * bin/kafka-console-producer.sh --broker-list localhost:9092 --topic t1
 * Created by xuwei
 */
public class StreamKafkaSourceJava {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //指定FlinkKafkaConsumer相关配置
        String topic = "t1";
        Properties prop = new Properties();
        prop.setProperty("bootstrap.servers","bigdata01:9092");
        prop.setProperty("group.id","con1");
        FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<>(topic, new SimpleStringSchema(), prop);

        //指定kafka作为source
        DataStreamSource<String> text = env.addSource(kafkaConsumer);

        //将读取到的数据打印到控制台
        text.print();

        env.execute("StreamKafkaSourceJava");
    }
}
