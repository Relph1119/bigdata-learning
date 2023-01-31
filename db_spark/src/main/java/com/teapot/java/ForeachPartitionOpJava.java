package com.teapot.java;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;

import java.util.Arrays;
import java.util.Iterator;

/**
 * 需求：foreachPartition的使用
 * Created by xuwei
 */
public class ForeachPartitionOpJava {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf.setAppName("ForeachPartitionOpJava")
                .setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<Integer> dataRDD = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5), 2);
        dataRDD.foreachPartition((VoidFunction<Iterator<Integer>>) it -> {
            System.out.println("=============");
            //在此处获取数据库链接
            while (it.hasNext()){
                //在这里使用数据库链接
                System.out.println(it.next());
            }
            //关闭数据库链接
        });

        sc.stop();
    }

}
