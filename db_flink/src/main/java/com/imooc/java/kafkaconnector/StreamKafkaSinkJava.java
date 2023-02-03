package com.imooc.java.kafkaconnector;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaSerializationSchemaWrapper;

import java.util.Properties;

/**
 * Flink向Kafka中生产数据
 * 创建t3的Topic：
 * cd /data/soft/kafka_2.12-2.4.1
 * bin/kafka-topics.sh --create --zookeeper localhost:2181 --partitions 1 --replication-factor 1 --topic t3
 *
 * 开启Socket：nc -l 9001
 * Created by xuwei
 */
public class StreamKafkaSinkJava {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> text = env.socketTextStream("bigdata01", 9001);

        //指定FlinkKafkaProducer相关配置
        String topic = "t3";
        Properties prop = new Properties();
        prop.setProperty("bootstrap.servers","bigdata01:9092");

        //指定kafak作为sink
        FlinkKafkaProducer<String> kafkaProducer = new FlinkKafkaProducer<>(
                topic,
                new KafkaSerializationSchemaWrapper<String>(topic, null, false, new SimpleStringSchema()),
                prop, FlinkKafkaProducer.Semantic.EXACTLY_ONCE);
        text.addSink(kafkaProducer);

        env.execute("StreamKafkaSinkJava");

    }
}
