package com.imooc.redis;

import redis.clients.jedis.Jedis;

/**
 * 单连接方式操作Redis
 * Created by xuwei
 */
public class RedisSingle {

    /**
     * 注意：此代码能够正常执行的前提是
     * 1：redis所在服务器的防火墙需要关闭
     * 2：redis.conf中的bind参数需要指定192.168.56.101
     *
     * @param args
     */
    public static void main(String[] args) {
        //获取jedis连接
        Jedis jedis = new Jedis("bigdata01", 6379);
        //使用密码
        //jedis.auth("admin");
        //向redis中添加数据 key=imooc，value=hello bigdata!
        jedis.set("imooc", "hello bigdata!");
        //从redis中查询key=imooc的value的值
        String value = jedis.get("imooc");

        System.out.println(value);

        //关闭jedis连接
        jedis.close();
    }
}
