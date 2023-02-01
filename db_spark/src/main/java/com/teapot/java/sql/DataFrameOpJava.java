package com.teapot.java.sql;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.col;

/**
 * 需求：DataFrame常见操作
 * Created by xuwei
 */
public class DataFrameOpJava {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf.setMaster("local");

        //创建SparkSession对象，里面包含SparkContext和SqlContext
        SparkSession sparkSession = SparkSession.builder()
                .appName("DataFrameOpJava")
                .config(conf)
                .getOrCreate();
        //读取json文件，获取Dataset<Row>
        String stuPath = DataFrameOpJava.class.getClassLoader().getResource("data/student.json").getPath();
        Dataset<Row> stuDf = sparkSession.read().json(stuPath);

        //打印schema信息
        stuDf.printSchema();

        //默认显示所有数据，可以通过参数控制显示多少条
        stuDf.show(2);

        //查询数据中的指定字段信息
        stuDf.select("name", "age").show();

        //在使用select的时候可以对数据做一些操作，需要引入 import static org.apache.spark.sql.functions.col;
        stuDf.select(col("name"), col("age").plus(1)).show();

        //对数据进行过滤
        stuDf.filter(col("age").gt(18)).show();
        stuDf.where(col("age").gt(18)).show();

        //对数据进行分组求和
        stuDf.groupBy("age").count().show();

        sparkSession.stop();

    }
}
