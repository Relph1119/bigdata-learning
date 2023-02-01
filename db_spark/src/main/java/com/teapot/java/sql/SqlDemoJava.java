package com.teapot.java.sql;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * 需求：使用json文件创建DataFrame
 * Created by xuwei
 */
public class SqlDemoJava {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf.setMaster("local");

        //创建SparkSession对象，里面包含SparkContext和SqlContext
        SparkSession sparkSession = SparkSession.builder()
                .appName("SqlDemoJava")
                .config(conf)
                .getOrCreate();
        //读取json文件，获取Dataset<Row>
        String stuPath = SqlDemoJava.class.getClassLoader().getResource("data/student.json").getPath();
        Dataset<Row> stuDf = sparkSession.read().json(stuPath).toDF();

        stuDf.show();

        sparkSession.stop();
    }

}
