/*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/1/25
 *
 * Class name:SuccessKilledServiceImpl
 */
package com.bennu.seckill.service.impl;

import com.bennu.seckill.entity.SuccessKilled;
import com.bennu.seckill.enums.SeckillStatEnum;
import com.bennu.seckill.mapper.SuccessKilledMapper;
import com.bennu.seckill.service.SuccessKilledService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class SuccessKilledServiceImpl implements SuccessKilledService {

    @Resource
    private SuccessKilledMapper successKilledMapper;

    @Override
    public int deleteByPrimaryKey(Long seckillId, Long userPhone) {
        return successKilledMapper.deleteByPrimaryKey(seckillId, userPhone);
    }

    @Override
    public int insert(SuccessKilled record) {
        return successKilledMapper.insert(record);
    }

    @Override
    public int insertSelective(SuccessKilled record) {
        return successKilledMapper.insertSelective(record);
    }

    @Override
    public SuccessKilled selectByPrimaryKey(Long seckillId, Long userPhone) {
        return successKilledMapper.selectByPrimaryKey(seckillId, userPhone);
    }

    @Override
    public int updateByPrimaryKeySelective(SuccessKilled record) {
        return successKilledMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SuccessKilled record) {
        return successKilledMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateBatch(List<SuccessKilled> list) {
        return successKilledMapper.updateBatch(list);
    }

    @Override
    public int batchInsert(List<SuccessKilled> list) {
        return successKilledMapper.batchInsert(list);
    }

    @Override
    public int insertSuccessKilled(long seckillId, long userPhone) {
        SuccessKilled successKilled = new SuccessKilled();
        successKilled.setSeckillId(seckillId);
        successKilled.setUserPhone(userPhone);
        successKilled.setCreateTime(LocalDateTime.now());
        successKilled.setState(SeckillStatEnum.END.getState());
        return successKilledMapper.insertSuccessKilled(successKilled);
    }

    @Override
    public int reduceNumber(long seckillId, Date nowTime) {
        return successKilledMapper.reduceNumber(seckillId, nowTime);
    }

    @Override
    public SuccessKilled queryByIdWithSeckill(long seckillId, long userPhone) {
        return successKilledMapper.queryByIdWithSeckill(seckillId, userPhone);
    }

}
