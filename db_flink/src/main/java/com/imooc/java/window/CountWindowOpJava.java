package com.imooc.java.window;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * CountWindow的使用
 * 1：滚动窗口
 * 2：滑动窗口
 * Created by xuwei
 */
public class CountWindowOpJava {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> text = env.socketTextStream("bigdata01", 9001);

        //CountWindow之滚动窗口：每隔5个元素计算一次前5个元素
        /*text.flatMap(new FlatMapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public void flatMap(String line, Collector<Tuple2<String, Integer>> out)
                    throws Exception {
                String[] words = line.split(" ");
                for (String word : words) {
                    out.collect(new Tuple2<String, Integer>(word,1));
                }
            }
        }).keyBy(0)
                //窗口大小
                .countWindow(5)
                .sum(1)
                .print();*/

        //CountWindow之滑动窗口：每隔1个元素计算一次前5个元素
        text.flatMap(new FlatMapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public void flatMap(String line, Collector<Tuple2<String, Integer>> out)
                    throws Exception {
                String[] words = line.split(" ");
                for (String word : words) {
                    out.collect(new Tuple2<String, Integer>(word,1));
                }
            }
        }).keyBy(0)
                //第一个参数：窗口大小，第二个参数：滑动间隔
                .countWindow(5,1)
                .sum(1)
                .print();

        env.execute("CountWindowOpJava");
    }
}
