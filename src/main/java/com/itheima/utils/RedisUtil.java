package com.itheima.utils;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisUtil {

    private static JedisPool jedisPool = null;
    private RedisUtil() { }

    static {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxIdle(20);
        config.setMaxTotal(5);

        jedisPool = new JedisPool(config, "192.168.66.81", 6379);
    }

    public static Jedis getJedis() {

        Jedis resource = jedisPool.getResource();
        return resource;
    }

}
