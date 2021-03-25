/*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/3/24
 *
 * Class name:RedisServiceImplTest
 */
package com.bennu.seckill.service.impl;

import com.bennu.seckill.entity.Seckill;
import com.bennu.seckill.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Rollback(false)
class RedisServiceImplTest {
    @Autowired
    private RedisService redisService;

    @Test
    void getSeckill() {
        Seckill seckill = new Seckill();
        seckill.setSeckillId(666L);
        Seckill result = redisService.getSeckill(seckill.getSeckillId());
        assertThat(result.getSeckillId()).isEqualTo(666L);
        assertThat(result.getName()).isEqualTo("666");
        assertThat(result.getNumber()).isEqualTo(666);
    }

    @Test
    void putSeckill() {
        Seckill seckill = new Seckill();
        seckill.setSeckillId(666L);
        seckill.setName("666");
        seckill.setNumber(666);
        seckill.setStartTime(LocalDateTime.now());
        seckill.setEndTime(LocalDateTime.now());
        seckill.setCreateTime(LocalDateTime.now());
        redisService.putSeckill(seckill);
    }

    @Test
    void setAndGet() {
        Seckill seckill = new Seckill();
        seckill.setSeckillId(666L);
        seckill.setName("666");
        seckill.setNumber(666);
        seckill.setStartTime(LocalDateTime.now());
        seckill.setEndTime(LocalDateTime.now());
        seckill.setCreateTime(LocalDateTime.now());
        redisService.putSeckill(seckill);
        Seckill result = redisService.getSeckill(seckill.getSeckillId());
        assertThat(result.getSeckillId()).isEqualTo(666L);
        assertThat(result.getName()).isEqualTo("666");
        assertThat(result.getNumber()).isEqualTo(666);
    }
}