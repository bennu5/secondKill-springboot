package com.bennu.seckill.service;

import com.bennu.seckill.entity.Seckill;
import com.github.pagehelper.Page;

import java.util.List;

/*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/1/25
 *
 * Class name:SeckillService
 */
public interface SeckillService {


    int deleteByPrimaryKey(Long seckillId);

    int insert(Seckill record);

    int insertSelective(Seckill record);

    Seckill selectByPrimaryKey(Long seckillId);

    int updateByPrimaryKeySelective(Seckill record);

    int updateByPrimaryKey(Seckill record);

    int updateBatch(List<Seckill> list);

    int batchInsert(List<Seckill> list);

    /**
     * 分页查询秒杀记录
     *
     * @return 秒杀记录
     */
    Page<Seckill> getSeckillList();
}
