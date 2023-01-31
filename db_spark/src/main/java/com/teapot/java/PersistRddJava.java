package com.teapot.java;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * 需求：RDD持久化
 * Created by xuwei
 */
public class PersistRddJava {

    public static void main(String[] args) {
        //创建JavaSparkContext
        SparkConf conf = new SparkConf();
        conf.setAppName("PersistRddJava")
                .setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> dataRDD = sc.textFile("D:\\hello_10000000.dat").cache();

        long start_time = System.currentTimeMillis();
        long count = dataRDD.count();
        System.out.println(count);
        long end_time = System.currentTimeMillis();
        System.out.println("第一次耗时："+(end_time-start_time));


        start_time = System.currentTimeMillis();
        count = dataRDD.count();
        System.out.println(count);
        end_time = System.currentTimeMillis();
        System.out.println("第二次耗时："+(end_time-start_time));

        sc.stop();
    }

}
