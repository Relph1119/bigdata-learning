package com.imooc.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * 需求：Java代码实现生产者代码
 * Created by xuwei
 */
public class ProducerDemo {
    public static void main(String[] args) {
        Properties prop = new Properties();
        //指定kafka的broker地址
        prop.put("bootstrap.servers", "bigdata01:9092");
        //指定key-value数据的序列化格式
        prop.put("key.serializer", StringSerializer.class.getName());
        prop.put("value.serializer", StringSerializer.class.getName());
        //创建Kafka生产者
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(prop);

        //指定topic
        String topic = "hello";

        //向topic中生产数据
        producer.send(new ProducerRecord<>(topic, "hello kafka"));

        //关闭链接
        producer.close();

    }

}
