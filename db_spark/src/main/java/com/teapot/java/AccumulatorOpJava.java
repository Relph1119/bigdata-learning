package com.teapot.java;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.util.LongAccumulator;

import java.util.Arrays;

/**
 * 需求：使用累加变量
 * Created by xuwei
 */
public class AccumulatorOpJava {

    public static void main(String[] args) {
        //创建JavaSparkContext
        SparkConf conf = new SparkConf();
        conf.setAppName("AccumulatorOpJava")
                .setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<Integer> dataRDD = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
        //1：定义累加变量
        LongAccumulator sumAccumulator = sc.sc().longAccumulator();

        //2：使用累加变量
        dataRDD.foreach((VoidFunction<Integer>) sumAccumulator::add);

        //获取累加变量的值
        System.out.println(sumAccumulator.value());


        sc.stop();
    }

}
