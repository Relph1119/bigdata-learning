package com.teapot.java;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.storage.StorageLevel;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

/**
 * 需求：Kryo序列化的使用
 * Created by xuwei
 */
public class KryoSerJava {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf.setAppName("KryoSerJava")
                .setMaster("local")
                .set("spark.serializer","org.apache.spark.serializer.KryoSerializer")
                .set("spark.kryo.classesToRegister","com.imooc.java.Person");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> dataRDD = sc.parallelize(Arrays.asList("hello you", "hello me"));

        JavaRDD<String> wordsRDD = dataRDD.flatMap((FlatMapFunction<String, String>) line -> Arrays.asList(line.split(" ")).iterator());
        JavaRDD<Person> personRDD = wordsRDD.map((Function<String, Person>) word -> new Person(word, 18)).persist(StorageLevel.MEMORY_ONLY_SER());

        personRDD.foreach((VoidFunction<Person>) System.out::println);

        while (true){
        }

    }
}

class Person implements Serializable{
    private final String name;
    private final int age;
    Person(String name,int age){
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
