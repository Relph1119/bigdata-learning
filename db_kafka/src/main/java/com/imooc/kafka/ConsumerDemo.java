package com.imooc.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

/**
 * 需求：使用Java代码实现消费者代码
 * 1: 关闭kafka服务器的防火墙
 * 2：配置windows的hosts文件，添加kafka节点的hostname和ip的映射关系
 * Created by xuwei
 */
public class ConsumerDemo {
    public static void main(String[] args) {
        Properties prop = new Properties();
        //指定kafka的broker地址
        prop.put("bootstrap.servers", "bigdata01:9092");
        //指定key-value的反序列化类型
        prop.put("key.deserializer", StringDeserializer.class.getName());
        prop.put("value.deserializer", StringDeserializer.class.getName());

        //指定消费者组
        prop.put("group.id", "con-1");

        //开启自动提交offset功能，默认就是开启的
        prop.put("enable.auto.commit", "true");
        //自动提交offset的时间间隔，单位是毫秒
        prop.put("auto.commit.interval.ms", "5000");
        /*
        注意：正常情况下，kafka消费数据的流程是这样的
        先根据group.id指定的消费者组到kafka中查找之前保存的offset信息
        如果查找到了，说明之前使用这个消费者组消费过数据，则根据之前保存的offset继续进行消费
        如果没查找到（说明第一次消费），或者查找到了，但是查找到的那个offset对应的数据已经不存在了
        这个时候消费者该如何消费数据？
        (因为kafka默认只会保存7天的数据，超过时间数据会被删除)

        此时会根据auto.offset.reset的值执行不同的消费逻辑

        这个参数的值有三种:[earliest,latest,none]
        earliest：表示从最早的数据开始消费(从头消费)
        latest【默认】: 表示从最新的数据开始消费
        none：如果根据指定的group.id没有找到之前消费的offset信息，就会抛异常

        解释：【查找到了，但是查找到的那个offset对应的数据已经不存在了】
        假设你第一天使用一个消费者去消费了一条数据，然后就把消费者停掉了
        等了7天之后，你又使用这个消费者去消费数据
        这个时候，这个消费者启动的时候会到kafka里面查询它之前保存的offset信息
        但是那个offset对应的数据已经被删了，所以此时再根据这个offset去消费是消费不到数据的。


        总结：一般在实时计算的场景下，这个参数的值建议设置为latest，消费最新的数据

        这个参数只有在消费者第一次消费数据，或者之前保存的offset信息已过期的情况下才会生效
         */
        prop.put("auto.offset.reset", "earliest");

        //创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(prop);
        Collection<String> topics = new ArrayList<>();
        topics.add("hello");
        //订阅指定的topic
        consumer.subscribe(topics);

        while (true) {
            //消费数据【注意：需要修改jdk的编译级别为1.8，否则Duration.ofSeconds(1)会语法报错】
            ConsumerRecords<String, String> poll = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> consumerRecord : poll) {
                System.out.println(consumerRecord);
            }

            consumer.commitSync();
        }

    }
}
