package com.imooc.redis;

import redis.clients.jedis.Jedis;

/**
 * Created by xuwei
 */
public class TestRedisUtils {
    public static void main(String[] args) {
        //获取连接
        Jedis jedis = RedisUtils.getJedis();
        String value = jedis.get("imooc");
        System.out.println(value);
        RedisUtils.returnResource(jedis);
        //向连接池返回连接
    }
}
