/*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/1/20
 *
 * Class name:SecondKillService
 */
package com.bennu.seckill.service;

import com.bennu.seckill.dto.Exposer;
import com.bennu.seckill.dto.SeckillExecution;
import com.bennu.seckill.entity.Seckill;
import com.bennu.seckill.exception.RepeatKillException;
import com.bennu.seckill.exception.SeckillCloseException;
import com.bennu.seckill.exception.SeckillException;
import com.github.pagehelper.Page;

/**
 * 业务接口：站在“使用者”的角度设计接口
 * 三个方面：方法定义粒度，参数，返回类型（return 类型/异常）
 *
 * @author bennu5
 * @version v1.0
 * @date 2021/3/25
 **/
public interface SecondKillService {
    /**
     * 查询所有秒杀记录
     *
     * @return 所有秒杀记录
     */
    Page<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     *
     * @param seckillId 秒杀Id
     * @return 秒杀记录
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开启时输出秒杀接口地址，否则输出系统时间和秒杀开启时间
     *
     * @param seckillId 秒杀Id
     * @return 秒杀记录
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     *
     * @param seckillId 秒杀Id
     * @param userPhone 手机号
     * @param md5       md5值
     * @return 秒杀结果
     * @throws SeckillException      秒杀异常
     * @throws RepeatKillException   重复秒杀异常
     * @throws SeckillCloseException 秒杀关闭异常
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException;
}
