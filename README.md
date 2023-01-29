# 大数据学习笔记
主要记录大数据学习的相关笔记，包括Hadoop、Flume

## 在线阅读地址
在线阅读地址：https://relph1119.github.io/bigdata-learning

## 环境安装
- Java版本：1.8  
- Ubuntu版本：20.04  
- Vbox版本：6.1.28 r147628 (Qt5.6.2)
- 虚拟机配置：显存大小50MB，内存大小4GB，硬盘大小（动态）50GB

### 环境准备
1. [Vbox配置Ubuntu的内外网访问](https://www.bilibili.com/video/av635603180/?vd_source=f4026a4ceb494a56ed0e12df39ea2d37)：主要使用NAT和Host-Only保证内外网的访问。
2. 在hosts文件中配置域名：bigdata01 {仅主机(Host-Only)网络的IP地址}

### 大数据组件版本

- Hadoop版本：3.2.0
- Flume版本：1.9.0

### 本地启动docsify
```shell
docsify serve ./docs
```

## 学习路线
1. **建议**从第01周第5章开始学习，可以用1.75倍的速度看视频
2. 第06周第4章内容，可以不用学习CDH和HDP的部署安装
3. 第07周第2章内容，由于机器不够，没有进行采集日志上传至HDFS的案例实验
