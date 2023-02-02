package com.imooc.java.tablesql;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;

import static org.apache.flink.table.api.Expressions.$;

/**
 * TableAPI 和 SQL的使用
 * Created by xuwei
 */
public class TableAPIAndSQLOpJava {
    public static void main(String[] args) {
        //获取TableEnvironment对象
        EnvironmentSettings sSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        TableEnvironment sTableEnv = TableEnvironment.create(sSettings);

        //创建输入表
        String dataSourceDir = TableAPIAndSQLOpJava.class.getClassLoader().getResource("data/source").getPath();
        sTableEnv.executeSql("" +
                "create table myTable(\n" +
                "id int,\n" +
                "name string\n" +
                ") with (\n" +
                "'connector.type' = 'filesystem',\n" +
                "'connector.path' = '" + dataSourceDir + "',\n" +
                "'format.type' = 'csv'\n" +
                ")");

        //使用TableAPI实现数据查询和过滤等操作
        /*Table result = sTableEnv.from("myTable")
                .select($("id"), $("name"))
                .filter($("id").isGreater(1));*/

        //使用SQL实现数据查询和过滤等操作
        Table result = sTableEnv.sqlQuery("select id,name from myTable where id > 1");

        //输出结果到控制台
        result.execute().print();

        //创建输出表
        String dataResDir = TableAPIAndSQLOpJava.class.getClassLoader().getResource("data/res").getPath();
        sTableEnv.executeSql("" +
                "create table newTable(\n" +
                "id int,\n" +
                "name string\n" +
                ") with (\n" +
                "'connector.type' = 'filesystem',\n" +
                "'connector.path' = '" + dataResDir + "',\n" +
                "'format.type' = 'csv'\n" +
                ")");

        //输出结果到表newTable中
        result.executeInsert("newTable");

    }
}
