#!/usr/bin/python
# -*- coding: UTF-8 -*-
import os
import sys
import json
import codecs
import subprocess
import time
import pymysql
# 创建目标数据库连接
def getConn():
      #连接数据库
      conn = pymysql.connect(host='xx.xx.xx.xx', user='root', passwd='root', db='xxx', port=3306)
      return conn
#创建执行SQL的函数
def executeSql(sql):
      #获取数据库连接
      conn = getConn()
      # 使用cursor()方法获取操作游标 
      cursor = conn.cursor()
      # 使用execute方法执行SQL语句
      cursor.execute(sql)
      # 使用 fetchone() 方法获取一条数据
      data = cursor.fetchall()
      # 关闭数据库连接
      cursor.close()
      conn.close()
      return data
#创建查询SQL的函数
def querySql(sql): 
      #获取数据库连接
      conn = getConn()
      # 使用cursor()方法获取操作游标 
      cursor = conn.cursor()
      # 使用execute方法执行SQL语句
      cursor.execute(sql)
      # 使用 fetchone() 方法获取所有数据
      data = cursor.fetchall()
      # 关闭数据库连接
      cursor.close()
      conn.close()
      return data
if __name__ == '__main__':
    tabname = sys.argv[0]
    collist_sql="""select group_concat(column_name) as column_list
          from (select column_name, ordinal_position
                  from information_schema. columns
                 where table_schema = 'hw_mbi'
                   and table_name = '${tabname}'
                 order by ordinal_position) t;
    """
    collist_sql = collist_sql.replace('${tabname}', tabname)
    printf("查询字段的SQL：%s"%collist_sql)
    collist_str = querySql(collist_sql)[0][0]
    printf("查询到的字段列表：%s"%collist_str)
    label_name = 'load_' + tabname + '_' + time.strftime("%Y%m%d_%H%M%S", time.localtime())
    load_sql=""" LOAD LABEL dm.${label_name}
(
DATA INFILE("hdfs://hadoopcluster/hive/warehouse/hw_dm.db/${tabname}/*")
INTO TABLE ${tabname} 
FORMAT AS "orc"
(${column_list})
)
WITH BROKER broker_name (
  "username"="xxx",
 "password"="xxx",
"dfs.nameservices" = "hadoopcluster",
"dfs.ha.namenodes.hadoopcluster" = "nn1,nn2",
"dfs.namenode.rpc-address.hadoopcluster.nn1" = "192.168.80.31:8020",
"dfs.namenode.rpc-address.hadoopcluster.nn2" = "192.168.80.32:8020",
"dfs.client.failover.proxy.provider" = "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider"                                       
) 
PROPERTIES (
"timeout" = "3600", 
"max_filter_ratio" = "0.1"--,"load_parallelism" = "8"
);
"""
    load_sql = load_sql.replace('${label_name}',label_name)\
                       .replace('${tabname}',tabname)\
                       .replace('${column_list}', collist_str)
    printf("加载数据的SQL：%s"%load_sql)
    executeSql(load_sql)
    try:
        while True:
            status_sql = "show load from dm where label ='${label_name}';"
            status_sql = status_sql.replace('${label_name}',label_name)
            printf("加载数据状态查询的SQL：%s"%status_sql)
            status_row = querySql(status_sql)[0]
            printf("状态查询到的数据：%s"%str(status_row))
            printf("状态查询到的状态：%s"%status_row[2])
            if status_row[2] == 'Finshed':
                  printf("%s load run sucess!" % label_name)
                  break
            elif status_row[2] == 'LOADIND' or status_row[2] == 'PENDING':
                  printf("%s load running! sleep 60s" % label_name)
                  time.sleep(60)
            else:
                  printf("%s load run failed!" % label_name)
                  printf("ErrorMsg: %s  异常数据查看URL:%s"%(status_row[7],status_row[13]))
                  break
      
    except Exception as e:
        traceback.print_exc() #输出程序错误位置
        os._exit(1) #异常退出，终止程序