package com.imooc.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 连接池的方式操作Redis
 * Created by xuwei
 */
public class RedisPool {
    public static void main(String[] args) {
        //创建连接池配置对象
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        //连接池中最大空闲连接数
        poolConfig.setMaxIdle(10);
        //连接池中创建的最大连接数
        poolConfig.setMaxTotal(100);
        //创建连接的超时时间
        poolConfig.setMaxWaitMillis(2000);
        //表示从连接池中获取连接的时候会先测试一下连接是否可用，这样可以保证取出的连接都是可用的
        poolConfig.setTestOnBorrow(true);

        //获取jedis连接池
        JedisPool jedisPool = new JedisPool(poolConfig, "bigdata01", 6379);

        //从jedis连接池中取出一个连接
        Jedis jedis = jedisPool.getResource();
        String value = jedis.get("imooc");
        System.out.println(value);

        //注意：此处的close方法有两层含义
        //1：如果jedis是直接创建的单连接，此时表示直接关闭这个连接
        //2：如果jedis是从连接池中获取的连接，此时会把这个连接返回给连接池
        jedis.close();

        //关闭jedis连接池
        jedisPool.close();
    }
}
