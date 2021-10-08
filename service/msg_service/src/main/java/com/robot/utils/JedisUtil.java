package com.robot.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {

    private static final JedisPool jedisPool;

    static {
        //设置redis地址,腾讯云公网ip
        String host = "101.42.92.192";
        //redis地址，腾讯云内网ip
        //String host = "172.21.16.5";
        //redis地址，虚拟机主机
        //String host = "192.168.200.128"
        //redis地址，本地主机
        //String host = "127.0.0.1"
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
