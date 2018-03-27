package com.anh.redis;

import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JedisTest {
    JedisCommands jedisCommands;
    JedisPool jedisPool;
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    String ip = "192.168.0.11";
    int port = 6379;
    int timeOut = 2000;

    public JedisTest() {
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setMaxTotal(1024);
        jedisPoolConfig.setMaxWaitMillis(100);
        jedisPoolConfig.setTestOnBorrow(false);
        jedisPoolConfig.setTestOnReturn(true);

        jedisPool = new JedisPool(jedisPoolConfig, ip, port, timeOut);

        Jedis jedis = jedisPool.getResource();
        jedis.hset("anhkey", "anhfield", "anhvalue");
        jedisCommands = jedis;
        jedis.select(2);
    }

    public void setValue(String key, String value) {
        this.jedisCommands.set(key, value);
    }

    public void setValue(String key, String field, String value) {
//        this.jedisCommands.hset(key, field, value);
    }

    public void setValue(String key, List<String> value) {
        for (String val : value) {
            this.jedisCommands.rpush(key, val);
        }

    }

    public String getValue(String key) {
        return this.jedisCommands.get(key);
    }

    public static void main(String[] args) {
        try {
            test0();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test0() {
        JedisTest jedisTest = new JedisTest();
        jedisTest.setValue("anh", "name", "csta11");
        List<String> list = new ArrayList<>();
        list.add("a1");
        list.add("a2");
        list.add("a3");
        list.add("a4");
        jedisTest.setValue("anhList", list);
        String value = jedisTest.getValue("name");
        System.out.println("value: " + value);
    }

    public static void test() throws Exception {
        JedisPoolConfig jpc = new JedisPoolConfig();
        jpc.setMaxTotal(2);
        jpc.setMaxIdle(1);
        jpc.setMaxWaitMillis(2000);
        jpc.setTestOnReturn(false);
        jpc.setTestOnBorrow(false);


        JedisShardInfo shardInfo = new JedisShardInfo("192.168.0.11", 6379, 500);
//        shardInfo.setPassword("123456");
        JedisShardInfo shardInfo1 = new JedisShardInfo("192.168.0.11", 6379, 500);
//        shardInfo1.setPassword("654321");

        List<JedisShardInfo> infoList = Arrays.asList(shardInfo, shardInfo1);
        ShardedJedisPool jedisPool = new ShardedJedisPool(jpc, infoList);
        ShardedJedis jedis = jedisPool.getResource();
        jedis.set("anhshard___1", "1");
        jedis.set("anhshard___2", "2");
        jedis.del("anhshard___2");
        String val = jedis.get("anhshard___1");
        System.out.println("val:" + val);
    }

}
