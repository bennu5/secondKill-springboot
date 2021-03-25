/*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/1/25
 *
 * Class name:SeckillServiceImpl
 */
package com.bennu.seckill.service.impl;

import com.bennu.seckill.entity.Seckill;
import com.bennu.seckill.mapper.SeckillMapper;
import com.bennu.seckill.service.SeckillService;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SeckillServiceImpl implements SeckillService {

    @Resource
    private SeckillMapper seckillMapper;

    @Override
    public int deleteByPrimaryKey(Long seckillId) {
        return seckillMapper.deleteByPrimaryKey(seckillId);
    }

    @Override
    public int insert(Seckill record) {
        return seckillMapper.insert(record);
    }

    @Override
    public int insertSelective(Seckill record) {
        return seckillMapper.insertSelective(record);
    }

    @Override
    public Seckill selectByPrimaryKey(Long seckillId) {
        return seckillMapper.selectByPrimaryKey(seckillId);
    }

    @Override
    public int updateByPrimaryKeySelective(Seckill record) {
        return seckillMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Seckill record) {
        return seckillMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateBatch(List<Seckill> list) {
        return seckillMapper.updateBatch(list);
    }

    @Override
    public int batchInsert(List<Seckill> list) {
        return seckillMapper.batchInsert(list);
    }

    @Override
    public Page<Seckill> getSeckillList() {
        return seckillMapper.getSeckillList();
    }

    @Override
    public int reduceNumber(long seckillId, Date nowTime) {
        return 0;
    }

    @Override
    public void killByProcedure(Map<String, Object> map) {

    }

}
