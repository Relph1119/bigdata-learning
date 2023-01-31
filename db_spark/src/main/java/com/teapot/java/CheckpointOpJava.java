package com.teapot.java;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;

/**
 * 需求：checkpoint的使用
 * Created by xuwei
 */
public class CheckpointOpJava {
    public static void main(String[] args) {
        //创建JavaSparkContext
        SparkConf conf = new SparkConf();
        conf.setAppName("CheckpointOpJava")
                .setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        if(args.length==0){
            System.exit(100);
        }
        String outputPath = args[0];

        //1：设置checkpoint目录
        sc.setCheckpointDir("hdfs://bigdata01:9000/chk001");

        JavaRDD<String> dataRDD = sc.textFile("hdfs://bigdata01:9000/hello_10000000.dat");

        //2：对rdd执行checkpoint操作
        dataRDD.checkpoint();

        dataRDD.flatMap((FlatMapFunction<String, String>) line -> Arrays.asList(line.split(" ")).iterator())
                .mapToPair((PairFunction<String, String, Integer>) word -> new Tuple2<>(word, 1))
                .reduceByKey((Function2<Integer, Integer, Integer>) Integer::sum)
                .saveAsTextFile(outputPath);

        sc.stop();
    }
}
