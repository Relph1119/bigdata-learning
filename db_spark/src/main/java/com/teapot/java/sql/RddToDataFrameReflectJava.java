package com.teapot.java.sql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

/**
 * 需求：通过反射方式实现RDD转换为DataFrame
 * Created by xuwei
 */
public class RddToDataFrameReflectJava {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf.setMaster("local");

        //创建SparkSession对象，里面包含SparkContext和SqlContext
        SparkSession sparkSession = SparkSession.builder()
                .appName("RddToDataFrameReflectJava")
                .config(conf)
                .getOrCreate();


        //获取SparkContext
        //从sparkSession中获取的是scala中的SparkContext，所以需要转换成java中的SparkContext
        JavaSparkContext sc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());
        Tuple2<String, Integer> t1 = new Tuple2<>("jack", 18);
        Tuple2<String, Integer> t2 = new Tuple2<>("tom", 20);
        Tuple2<String, Integer> t3 = new Tuple2<>("jessic", 30);
        JavaRDD<Tuple2<String, Integer>> dataRDD = sc.parallelize(Arrays.asList(t1, t2, t3));

        JavaRDD<Student> stuRDD = dataRDD.map((Function<Tuple2<String, Integer>, Student>) tup -> new Student(tup._1, tup._2));

        //注意：Student这个类必须声明为public，并且必须实现序列化
        Dataset<Row> stuDf = sparkSession.createDataFrame(stuRDD, Student.class);

        stuDf.createOrReplaceTempView("student");

        //执行sql查询
        Dataset<Row> resDf = sparkSession.sql("select name,age from student where age > 18");

        //将DataFrame转化为RDD，注意，这里需要转化为JavaRDD
        JavaRDD<Row> resRDD = resDf.javaRDD();
        //从row中取数据，封装成student，打印到控制台
        List<Student> resList = resRDD.map((Function<Row, Student>) row -> {
            //return new Student(row.getString(0),row.getInt(1));
            //通过getAs获取数据
            return new Student(row.getAs("name").toString(), Integer.parseInt(row.getAs("age").toString()));
        }).collect();

        for(Student stu: resList){
            System.out.println(stu);
        }

        sparkSession.stop();
    }
}
