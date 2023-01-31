package com.teapot.java;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 需求：Action实战
 * reduce：聚合计算
 * collect：获取元素集合
 * take(n)：获取前n个元素
 * count：获取元素总数
 * saveAsTextFile：保存文件
 * countByKey：统计相同的key出现多少次
 * foreach：迭代遍历元素
 * <p>
 * Created by xuwei
 */
public class ActionOpJava {

    public static void main(String[] args) {
        JavaSparkContext sc = getSparkContext();
        //reduce：聚合计算
        //reduceOp(sc);
        //collect：获取元素集合
        //collectOp(sc);
        //take(n)：获取前n个元素
        //takeOp(sc);
        //count：获取元素总数
        //countOp(sc);
        //saveAsTextFile：保存文件
        //saveAsTextFileOp(sc);
        //countByKey：统计相同的key出现多少次
        //countByKeyOp(sc);
        //foreach：迭代遍历元素
        //foreachOp(sc);

        sc.stop();
    }

    private static void foreachOp(JavaSparkContext sc) {
        JavaRDD<Integer> dataRDD = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
        dataRDD.foreach((VoidFunction<Integer>) System.out::println);
    }

    private static void countByKeyOp(JavaSparkContext sc) {
        Tuple2<String, Integer> t1 = new Tuple2<>("A", 1001);
        Tuple2<String, Integer> t2 = new Tuple2<>("B", 1002);
        Tuple2<String, Integer> t3 = new Tuple2<>("A", 1003);
        Tuple2<String, Integer> t4 = new Tuple2<>("C", 1004);

        JavaRDD<Tuple2<String, Integer>> dataRDD = sc.parallelize(Arrays.asList(t1, t2, t3, t4));
        //想要使用countByKey，需要先使用mapToPair对RDD进行转换
        Map<String, Long> res = dataRDD.mapToPair(
                (PairFunction<Tuple2<String, Integer>, String, Integer>) tup -> new Tuple2<>(tup._1, tup._2))
                .countByKey();
        for (Map.Entry<String, Long> entry : res.entrySet()) {
            System.out.println(entry.getKey() + "," + entry.getValue());
        }

    }

    private static void saveAsTextFileOp(JavaSparkContext sc) {
        JavaRDD<Integer> dataRDD = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
        dataRDD.saveAsTextFile("hdfs://bigdata01:9000/out002");
    }

    private static void countOp(JavaSparkContext sc) {
        JavaRDD<Integer> dataRDD = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
        long res = dataRDD.count();
        System.out.println(res);
    }

    private static void takeOp(JavaSparkContext sc) {
        JavaRDD<Integer> dataRDD = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> res = dataRDD.take(2);
        for (Integer item : res) {
            System.out.println(item);
        }
    }

    private static void collectOp(JavaSparkContext sc) {
        JavaRDD<Integer> dataRDD = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> res = dataRDD.collect();
        for (Integer item : res) {
            System.out.println(item);
        }
    }

    private static void reduceOp(JavaSparkContext sc) {
        JavaRDD<Integer> dataRDD = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
        Integer num = dataRDD.reduce((Function2<Integer, Integer, Integer>) Integer::sum);

        System.out.println(num);
    }

    private static JavaSparkContext getSparkContext() {
        //创建JavaSparkContext
        SparkConf conf = new SparkConf();
        conf.setAppName("ActionOpJava")
                .setMaster("local");
        return new JavaSparkContext(conf);
    }
}
