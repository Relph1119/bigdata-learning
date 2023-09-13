# 大数据学习笔记 {docsify-ignore-all}

主要记录大数据学习的相关笔记，包括Hadoop、Flume、Hive、Scala、Spark、Kafka、Redis、Flink、ClickHouse、Doris等各个组件的理论，并通过代码实验，了解组件的使用。

## 环境安装
- OpenJDK Java版本：1.8.0_352
- Scala版本：2.12.11
- Ubuntu版本：20.04
- Vbox版本：6.1.28 r147628 (Qt5.6.2)
- 虚拟机配置：显存大小50MB，内存大小4GB，硬盘大小（动态）50GB

### 环境准备
1. [Vbox配置Ubuntu的内外网访问](https://www.bilibili.com/video/av635603180/?vd_source=f4026a4ceb494a56ed0e12df39ea2d37)：主要使用NAT和Host-Only保证内外网的访问。
2. 关闭Ubuntu防火墙
   ```shell
   sudo apt-get install ufw
   ufw disable
   ```
2. 在hosts文件中配置域名：bigdata01 {仅主机(Host-Only)网络的IP地址}
   - 查看`VirtualBox Host-Only Ethernet Adapter`网卡的IP设置，笔者的电脑设置为192.168.56.1
   - 查看Vbox上仅主机(Host-Only)网络的IP地址，笔者的电脑设置为192.168.56.101，所有虚拟机的对外访问地址就是这个地址。
3. 配置环境变量，打开`/etc/profile`，在文件末尾添加以下内容：
    ```shell
    export HADOOP_HOME=/data/soft/hadoop-3.2.0
    export HIVE_HOME=/data/soft/apache-hive-3.1.2-bin
    export SPARK_HOME=/data/soft/spark-3.1.3-bin-hadoop3.2
    export HADOOP_CLASSPATH=`${HADOOP_HOME}/bin/hadoop classpath`
    export PATH=.:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$HIVE_HOME/bin:$SPARK_HOME/bin:$PATH
    ```

### 大数据组件版本

- Hadoop版本：3.2.0
- Flume版本：1.9.0
- Hive版本：3.1.2
- MySQL版本：8.0.32-0buntu0.20.04.1 (Ubuntu)
- Spark版本：3.1.3-bin-hadoop3.2
- Zookeeper版本：3.5.8
- Kafka版本：kafka_2.12-2.4.1
- Redis版本：5.0.9
- Flink版本：1.11.1
- ClickHouse版本：20.2.1

### 快速启动大数据组件

- 启动Hadoop
```shell
start-all.sh
mapred --daemon start historyserver
```

- 设置MySQL开机自启动
```shell
systemctl enable mysql.service
```

- 启动Hive
```shell
hiveserver2 &
```

- 启动Spark HistoryServer
```shell
cd /data/soft/spark-3.1.3-bin-hadoop3.2
sbin/start-history-server.sh
```

- 启动Zookeeper
```shell
cd /data/soft/apache-zookeeper-3.5.8-bin
bin/zkServer.sh start
```

- 启动Kafka
```shell
cd /data/soft/kafka_2.12-2.4.1
bin/kafka-server-start.sh -daemon config/server.properties
```

- 启动Redis
```shell
cd /data/soft/redis-5.0.9/
redis-server redis.conf
```

- 启动Flink日志进程
```shell
cd /data/soft/flink-1.11.1
bin/historyserver.sh start
```

- 启动ClickHouse
```shell
sudo /etc/init.d/clickhouse-server start
```

- 启动Doris
```shell
cd /data/soft/apache-doris-1.2.7-bin-x64/fe
./bin/start_fe.sh --daemon
sysctl -w vm.max_map_count=2000000
ulimit -n 65536
cd /data/soft/apache-doris-1.2.7-bin-x64/be
./bin/start_be.sh --daemon
```

### 大数据组件默认端口

- Hadoop的HDFS webui界面：http://bigdata01:9870
- Hadoop的YARN webui界面：http://bigdata01:8088
- HDFS端口：9000
- MySQL端口：3306
- Hive端口：10000
- Spark History Server界面：http://bigdata01:18080/
- Zookeeper端口：2181
- Kafka端口：9092
- Redis端口：6379
- ClickHouse端口：8123  
- Doris端口：9030
- Doris元数据页面：http://192.168.56.101:8030/ ，用户名`root`，密码`root`

### 本地启动docsify
```shell
docsify serve ./docs
```

## 学习注意事项

1. **建议**从第01周第5章开始学习，可以用1.75倍的速度看视频
2. 第06周第4章内容，可以不用学习CDH和HDP的部署安装
3. 第07周第2章内容，由于机器不够，没有进行采集日志上传至HDFS的案例实验
4. 修改了db_spark的依赖库，使用对应Hadoop和Scala版本的库，并添加了log4j的配置文件，删除了红色的Log日志
5. 第12周前3章内容，可以重点听，后面代码实战内容可以快速观看，由于需要数据接口校验码，无法获取数据进行案例实战
6. 第13周主要学习第3章内容，其他内容可快速观看
7. 第17周第2章的Watermark理论部分有缺少，可以查看这篇文章[带你理解并使用flink中的WaterMark机制](https://blog.csdn.net/Chenftli/article/details/124274118)
8. 第18、19周的项目实战内容可以快速观看，由于需要数据接口校验码，无法获取数据进行案例实战

## 学习资料

【1】大数据体系课-慕课网2019年课程：学习注意事项提到的内容是来源于本资料的。  
【2】《ClickHouse性能之巅：从架构设计解读性能之谜》：第12章的内容来源于本书。  
【3】《Doris实时数仓实战》：第13、14章的内容来源于本书。