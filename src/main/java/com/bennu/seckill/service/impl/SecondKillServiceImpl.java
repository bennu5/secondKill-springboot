/*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/1/20
 *
 * Class name:SecondKillServiceImpl
 */
package com.bennu.seckill.service.impl;

import com.bennu.seckill.dto.Exposer;
import com.bennu.seckill.dto.SeckillExcution;
import com.bennu.seckill.entity.Seckill;
import com.bennu.seckill.exception.RepeatKillException;
import com.bennu.seckill.exception.SeckillCloseException;
import com.bennu.seckill.exception.SeckillException;
import com.bennu.seckill.service.SeckillService;
import com.bennu.seckill.service.SecondKillService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 功能描述
 *
 * @author bennu5
 * @version v1.0
 * @date 2021/1/20
 **/
@Service
@Slf4j
public class SecondKillServiceImpl implements SecondKillService {
    private final SeckillService seckillService;

    @Autowired
    public SecondKillServiceImpl(SeckillService seckillService) {
        this.seckillService = seckillService;
    }

    @Override
    public Page<Seckill> getSeckillList() {
        PageHelper.startPage(1, 10);
        return seckillService.getSeckillList();
    }

    @Override
    public Seckill getById(long seckillId) {
        return null;
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        return null;
    }

    @Override
    public SeckillExcution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        return null;
    }

    @Override
    public SeckillExcution executeSeckillByProcedure(long seckillId, long userPhone, String md5) {
        return null;
    }
}
