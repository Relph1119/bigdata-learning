package com.teapot.java;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.Arrays;

/**
 * 需求：设置并行度
 * Created by xuwei
 */
public class MoreParallelismJava {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf.setAppName("MoreParallelismJava");
        //设置全局并行度
        conf.set("spark.default.parallelism","5");
        JavaSparkContext sc = new JavaSparkContext(conf);


        JavaRDD<String> dataRDD = sc.parallelize(Arrays.asList("hello", "you", "hello", "me", "hehe", "hello", "you", "hello", "me", "hehe"));
        dataRDD.mapToPair((PairFunction<String, String, Integer>) word -> new Tuple2<>(word, 1))
                .reduceByKey((Function2<Integer, Integer, Integer>) Integer::sum)
                .foreach((VoidFunction<Tuple2<String, Integer>>) System.out::println);

        sc.stop();
    }

}
