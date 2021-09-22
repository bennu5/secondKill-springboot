package com.bennu.seckill.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RedisConfigTest {
    @Test
    private void connect(){
        RedisClient redisClient = RedisClient.create("redis://1Jdfdsj#@$DFSWdsd@#%sdfsd#fd@82.157.1.224:6379/");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
    }
}