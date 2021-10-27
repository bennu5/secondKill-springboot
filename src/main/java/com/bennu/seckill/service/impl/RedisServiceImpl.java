/*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/2/23
 *
 * Class name:RedisServiceImpl
 */
package com.bennu.seckill.service.impl;

import com.bennu.seckill.entity.Seckill;
import com.bennu.seckill.entity.SuccessKilled;
import com.bennu.seckill.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 从Redis缓存存储和读取秒杀记录
 *
 * @author bennu5
 * @version v1.0
 * @date 2021/2/23
 **/
@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Seckill getSeckill(long seckillId) {
        String key = "seckill:" + seckillId;
        return (Seckill) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void putSeckill(Seckill seckill) {
        String key = "seckill:" + seckill.getSeckillId();
        redisTemplate.opsForValue().set(key, seckill);
    }

    @Override
    public void putSeckillRecord(String recordKey, SuccessKilled successKilled) {
        redisTemplate.opsForHash().put(recordKey, successKilled.getSeckillId() + String.valueOf(successKilled.getUserPhone()), successKilled);
    }

    @Override
    public void putSeckillRecord(String recordKey, Map<String, SuccessKilled> map) {
        redisTemplate.opsForHash().putAll(recordKey, map);
    }

    @Override
    public Boolean checkKey(String recordKey) {
        return redisTemplate.hasKey(recordKey);
    }

    @Override
    public boolean checkRecord(String recordKey, String hashKey) {
        return redisTemplate.opsForHash().hasKey(recordKey, hashKey);
    }
}
