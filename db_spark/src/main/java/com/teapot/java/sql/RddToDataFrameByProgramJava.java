package com.teapot.java.sql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.sources.In;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 需求：使用编程方式实现RDD转换为DataFrame
 * Created by xuwei
 */
public class RddToDataFrameByProgramJava {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf.setMaster("local");

        //创建SparkSession对象，里面包含SparkContext和SqlContext
        SparkSession sparkSession = SparkSession.builder()
                .appName("RddToDataFrameByProgramJava")
                .config(conf)
                .getOrCreate();


        //获取SparkContext
        //从sparkSession中获取的是scala中的SparkContext，所以需要转换成java中的SparkContext
        JavaSparkContext sc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());
        Tuple2<String, Integer> t1 = new Tuple2<>("jack", 18);
        Tuple2<String, Integer> t2 = new Tuple2<>("tom", 20);
        Tuple2<String, Integer> t3 = new Tuple2<>("jessic", 30);
        JavaRDD<Tuple2<String, Integer>> dataRDD = sc.parallelize(Arrays.asList(t1, t2, t3));

        //组装RDD
        JavaRDD<Row> rowRDD = dataRDD.map(
                (Function<Tuple2<String, Integer>, Row>) tup -> RowFactory.create(tup._1, tup._2));

        //指定元数据信息
        ArrayList<StructField> structFieldList = new ArrayList<>();
        structFieldList.add(DataTypes.createStructField("name", DataTypes.StringType, true));
        structFieldList.add(DataTypes.createStructField("age", DataTypes.IntegerType, true));

        StructType schema = DataTypes.createStructType(structFieldList);

        //构建DataFrame
        Dataset<Row> stuDf = sparkSession.createDataFrame(rowRDD, schema);

        stuDf.createOrReplaceTempView("student");
        Dataset<Row> resDf = sparkSession.sql("select name,age from student where age > 18");

        JavaRDD<Row> resRDD = resDf.javaRDD();

        List<Tuple2<String, Integer>> resList = resRDD.map(
                (Function<Row, Tuple2<String, Integer>>) row -> new Tuple2<>(row.getString(0), row.getInt(1)))
                .collect();

        for (Tuple2<String, Integer> tup : resList) {
            System.out.println(tup);
        }
        sparkSession.stop();
    }

}
