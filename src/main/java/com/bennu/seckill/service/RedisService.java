/*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/3/24
 *
 * Class name:RedisService
 */
package com.bennu.seckill.service;

import com.bennu.seckill.entity.Seckill;
import com.bennu.seckill.entity.SuccessKilled;

import java.util.Map;

/**
 * 从Redis缓存存储和读取秒杀记录
 *
 * @author bennu5
 * @version v1.0
 * @date 2021/2/23
 **/
public interface RedisService {
    /**
     * 从Redis中获取秒杀对象信息
     *
     * @param seckillId 秒杀ID
     * @return 秒杀对象信息
     */
    Seckill getSeckill(long seckillId);

    /**
     * 向Redis中存储秒杀对象信息
     *
     * @param seckill 秒杀对象信息
     */
    void putSeckill(Seckill seckill);

    /**
     * 向Redis中存储秒杀记录对象信息
     * @param recordKey 秒杀记录key
     * @param successKilled 秒杀记录对象
     */
    void putSeckillRecord(String recordKey, SuccessKilled successKilled);

    /**
     * 向Redis中初始化秒杀记录对象信息
     * @param recordKey 秒杀记录key
     * @param map 秒杀记录对象信息
     */
    void putSeckillRecord(String recordKey, Map<String, SuccessKilled> map);
}
