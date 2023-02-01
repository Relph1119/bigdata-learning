package com.teapot.java.sql;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * Created by xuwei
 */
public class DataFrameSqlJava {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf.setMaster("local");

        //创建SparkSession对象，里面包含SparkContext和SqlContext
        SparkSession sparkSession = SparkSession.builder()
                .appName("DataFrameSqlJava")
                .config(conf)
                .getOrCreate();
        String stuPath = DataFrameSqlJava.class.getClassLoader().getResource("data/student.json").getPath();
        Dataset<Row> stuDf = sparkSession.read().json(stuPath);

        //将Dataset<Row>注册为一个临时表
        stuDf.createOrReplaceTempView("student");

        //使用sql查询临时表中的数据
        sparkSession.sql("select age,count(*) as num from student group by age")
                .show();

        sparkSession.stop();
    }
}
