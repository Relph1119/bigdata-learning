package com.imooc.java.stream.transformation;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;

/**
 * 分区规则的使用
 * Created by xuwei
 */
public class StreamPartitionOpJava {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Integer> text = env.fromCollection(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        //使用shuffle分区规则
        //shuffleOp(text);

        //使用rebalance分区规则
        //rebalanceOp(text);

        //使用rescale分区规则
        //rescaleOp(text);

        //使用broadcast分区规则
        //broadcastOp(text);

        //自定义分区规则
        //custormPartitionOp(text);

        env.execute("StreamPartitionOpJava");
    }

    private static void custormPartitionOp(DataStreamSource<Integer> text) {
        text.map(new MapFunction<Integer, Integer>() {
            @Override
            public Integer map(Integer value) throws Exception {
                return value;
            }
        }).setParallelism(2)
                .partitionCustom(new MyPartitionerJava(), new KeySelector<Integer, Integer>() {
                    @Override
                    public Integer getKey(Integer value) throws Exception {
                        return value;
                    }
                })
                .print()
                .setParallelism(4);
    }

    private static void broadcastOp(DataStreamSource<Integer> text) {
        text.map(new MapFunction<Integer, Integer>() {
            @Override
            public Integer map(Integer value) throws Exception {
                return value;
            }
        }).setParallelism(2)
                .broadcast()
                .print()
                .setParallelism(4);
    }

    private static void rescaleOp(DataStreamSource<Integer> text) {
        text.map(new MapFunction<Integer, Integer>() {
            @Override
            public Integer map(Integer value) throws Exception {
                return value;
            }
        }).setParallelism(2)
                .rescale()
                .print()
                .setParallelism(4);
    }

    private static void rebalanceOp(DataStreamSource<Integer> text) {
        text.map(new MapFunction<Integer, Integer>() {
            @Override
            public Integer map(Integer value) throws Exception {
                return value;
            }
        }).setParallelism(2)
                .rebalance()
                .print()
                .setParallelism(4);
    }

    private static void shuffleOp(DataStreamSource<Integer> text) {
        text.map(new MapFunction<Integer, Integer>() {
            @Override
            public Integer map(Integer value) throws Exception {
                return value;
            }
        }).setParallelism(2)
                .shuffle()
                .print()
                .setParallelism(4);
    }
}
