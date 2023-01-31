package com.teapot.java;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;

import java.util.Arrays;
import java.util.Iterator;

/**
 * 需求：repartition的使用
 * Created by xuwei
 */
public class RepartitionOpJava {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf.setAppName("RepartitionOpJava")
                .setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<Integer> dataRDD = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5), 2);

        dataRDD.repartition(3)
                .foreachPartition((VoidFunction<Iterator<Integer>>) it -> {
                    System.out.println("==============");
                    while (it.hasNext()){
                        System.out.println(it.next());
                    }
                });

        sc.stop();

    }
}
