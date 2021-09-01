package com.niceboom.weather.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {

    private static final JedisPool jedisPool;

    static {
        //设置redis地址
        String host = "127.0.0.1";
        int port = 6379;

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);

        jedisPool = new JedisPool(jedisPoolConfig, host, port);
    }

    private JedisUtil(){}

    public static Jedis getResource(){
        return jedisPool.getResource();
    }
}
