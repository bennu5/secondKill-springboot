package com.bennu.seckill.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RedisConfigTest {
    @Test
    void connect(){
        RedisClient redisClient = RedisClient.create("redis://1Jdf3dsjDF2SW5ds7ds9df3sd5fd@82.157.1.224:6379/0");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        System.out.println("Connected to Redis");
        connection.sync().set("key", "Hello World");
        connection.close();
        redisClient.shutdown();
    }

    @Test
    void getTest(){
        RedisClient redisClient = RedisClient.create("redis://1Jdf3dsjDF2SW5ds7ds9df3sd5fd@82.157.1.224:6379/0");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        System.out.println("Connected to Redis");
        String value = connection.sync().get("key");
        System.out.println("Get value from redis:" + value);
        connection.close();
        redisClient.shutdown();
    }
}