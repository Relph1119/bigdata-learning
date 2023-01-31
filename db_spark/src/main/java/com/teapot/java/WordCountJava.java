package com.teapot.java;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.Arrays;

/**
 * 需求：单词计数
 * Created by xuwei
 */
public class WordCountJava {
    public static void main(String[] args) {
        //第一步：创建JavaSparkContext
        SparkConf conf = new SparkConf();
        conf.setAppName("WordCountJava")
                .setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        //第二步：加载数据
        String path = WordCountJava.class.getClassLoader().getResource("data/hello.txt").getPath();
        if (args.length == 1) {
            path = args[0];
        }
        JavaRDD<String> linesRDD = sc.textFile(path);
        //第三步：对数据进行切割，把一行数据切分成一个一个的单词
        //注意：FlatMapFunction的泛型，第一个参数表示输入数据类型，第二个表示是输出数据类型
        JavaRDD<String> wordsRDD = linesRDD.flatMap(
                (FlatMapFunction<String, String>) line -> Arrays.asList(line.split(" ")).iterator());
        //第四步：迭代words，将每个word转换为(word,1)这种形式
        //注意：PairFunction的泛型，第一个参数是输入数据类型
        //第二个是输出tuple中的第一个参数类型，第三个是输出tuple中的第二个参数类型
        //注意：如果后面需要使用到....ByKey，前面都需要使用mapToPair去处理
        JavaPairRDD<String, Integer> pairRDD = wordsRDD.mapToPair(
                (PairFunction<String, String, Integer>) word -> new Tuple2<>(word, 1));
        //第五步：根据key(其实就是word)进行分组聚合统计
        JavaPairRDD<String, Integer> wordCountRDD = pairRDD.reduceByKey(
                (Function2<Integer, Integer, Integer>) Integer::sum);
        //第六步：将结果打印到控制台
        wordCountRDD.foreach((VoidFunction<Tuple2<String, Integer>>) tup -> System.out.println(tup._1 + "--" + tup._2));
        //第七步：停止SparkContext
        sc.stop();
    }
}
