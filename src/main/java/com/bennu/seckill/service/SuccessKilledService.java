/*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/1/25
 *
 * Class name:SuccessKilledService
 */
package com.bennu.seckill.service;

import com.bennu.seckill.entity.SuccessKilled;

import java.util.Date;
import java.util.List;


public interface SuccessKilledService {
    int deleteByPrimaryKey(Long seckillId, Long userPhone);

    int insert(SuccessKilled record);

    int insertSelective(SuccessKilled record);

    SuccessKilled selectByPrimaryKey(Long seckillId, Long userPhone);

    int updateByPrimaryKeySelective(SuccessKilled record);

    int updateByPrimaryKey(SuccessKilled record);

    int updateBatch(List<SuccessKilled> list);

    int batchInsert(List<SuccessKilled> list);

    /**
     * 新增秒杀成功记录
     *
     * @param seckillId 秒杀ID
     * @param userPhone 用户手机
     * @return 插入记录数
     */
    int insertSuccessKilled(long seckillId, long userPhone);

    /**
     * 修改商品梳理
     *
     * @param seckillId 秒杀ID
     * @param nowTime   当前时间
     * @return 修改结果
     */
    int reduceNumber(long seckillId, Date nowTime);

    SuccessKilled queryByIdWithSeckill(long seckillId, long userPhone);

    /**
     * 查询所有秒杀记录
     * @param seckillId 秒杀商品ID
     * @return 秒杀记录List
     */
    List<SuccessKilled> selectBySeckillId(long seckillId);
}
