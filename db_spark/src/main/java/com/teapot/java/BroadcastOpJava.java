package com.teapot.java;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.broadcast.Broadcast;

import java.util.Arrays;

/**
 * 需求：使用广播变量
 * Created by xuwei
 */
public class BroadcastOpJava {

    public static void main(String[] args) {
        //创建JavaSparkContext
        SparkConf conf = new SparkConf();
        conf.setAppName("BroadcastOpJava")
                .setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<Integer> dataRDD = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
        int variable = 2;
        //1：定义广播变量
        Broadcast<Integer> variableBroadcast = sc.broadcast(variable);

        //2：使用广播变量
        dataRDD.map((Function<Integer, Integer>) i1 -> i1 * variableBroadcast.value())
                .foreach((VoidFunction<Integer>) System.out::println);

        sc.stop();
    }
}
