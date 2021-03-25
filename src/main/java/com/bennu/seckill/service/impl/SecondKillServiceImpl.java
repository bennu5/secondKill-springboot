/*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/1/20
 *
 * Class name:SecondKillServiceImpl
 */
package com.bennu.seckill.service.impl;

import org.apache.commons.collections.MapUtils;
import com.bennu.seckill.dto.Exposer;
import com.bennu.seckill.dto.SeckillExecution;
import com.bennu.seckill.entity.Seckill;
import com.bennu.seckill.entity.SuccessKilled;
import com.bennu.seckill.enums.SeckillStatEnum;
import com.bennu.seckill.exception.RepeatKillException;
import com.bennu.seckill.exception.SeckillCloseException;
import com.bennu.seckill.exception.SeckillException;
import com.bennu.seckill.service.RedisService;
import com.bennu.seckill.service.SeckillService;
import com.bennu.seckill.service.SecondKillService;
import com.bennu.seckill.service.SuccessKilledService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    /**
     * 秒杀项目Service
     */
    private final SeckillService seckillService;
    /**
     * 成功秒杀记录Service
     */
    private final SuccessKilledService successKilledService;
    /**
     * Redis Service
     */
    private final RedisService redisService;

    @Autowired
    public SecondKillServiceImpl(SeckillService seckillService, SuccessKilledService successKilledService, RedisService redisService) {
        this.seckillService = seckillService;
        this.successKilledService = successKilledService;
        this.redisService = redisService;
    }


    @Override
    public Page<Seckill> getSeckillList() {
        PageHelper.startPage(1, 10);
        return seckillService.getSeckillList();
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillService.selectByPrimaryKey(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {

        // 优化点：缓存优化：超时的基础上进行一致性维护
        // 1: 访问 Redis
        Seckill seckill = redisService.getSeckill(seckillId);
        if (seckill == null) {
            // 2. 访问数据库
            seckill = seckillService.selectByPrimaryKey(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            } else {
                // 3. 放入 Redis
                redisService.putSeckill(seckill);
            }
        }
        // java8 日期类 LocalDateTime
        LocalDateTime startTime = seckill.getStartTime();
        LocalDateTime endTime = seckill.getEndTime();
        LocalDateTime nowTime = LocalDateTime.now();
        if (nowTime.isBefore(startTime) || nowTime.isAfter(endTime)) {
            return new Exposer(false, seckillId, nowTime.atZone(ZoneId.systemDefault()).toEpochSecond(), startTime.atZone(ZoneId.systemDefault()).toEpochSecond(), endTime.atZone(ZoneId.systemDefault()).toEpochSecond());
        }

        // 转换特定字符串,不可逆
        String md5 = getMd5(seckill.getSeckillId());
        return new Exposer(true, md5, seckillId);
    }

    /**
     * 加入盐值混淆，生成MD5，
     *
     * @param seckillId 秒杀ID
     * @return MD5
     */
    private String getMd5(long seckillId) {
        String slat = "sdhiondghdSGdhfiads@#%%^DFGdgk";
        String base = seckillId + "/" + slat;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    @Override
    @Transactional(rollbackFor = SeckillException.class)
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException {
        if (md5 == null || !md5.equals(getMd5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        // 执行秒杀逻辑 + 减库存
        Date nowTime = new Date();
        try {
            // 记录购买行为
            int insertCount = successKilledService.insertSuccessKilled(seckillId, userPhone);
            if (insertCount < 1) {
                // 重复秒杀
                throw new RepeatKillException("seckill repeated");
            } else {
                // 减库存，热点商品竞争
                int updateCount = successKilledService.reduceNumber(seckillId, nowTime);
                if (updateCount < 1) {
                    // 没有更新到记录，秒杀结束
                    throw new SeckillCloseException("seckill is closed!");
                } else {
                    SuccessKilled successKilled = successKilledService.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException | RepeatKillException e1) {
            throw e1;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // 所有编译期异常转换为运行期异常
            throw new SeckillException("秒杀出错");
        }
    }

    @Override
    public SeckillExecution executeSeckillByProcedure(long seckillId, long userPhone, String md5) {
        if (md5 != null && !md5.equals(getMd5(seckillId))) {
            return new SeckillExecution(seckillId, SeckillStatEnum.DATA_REWRITE);
        }
        Date killTime = new Date();
        Map<String, Object> map = new HashMap<>(5);
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", killTime);
        map.put("result", null);
        // 执行存储过程，result 被复制
        try {
            seckillService.killByProcedure(map);
            // 获取 result
            int result = MapUtils.getInteger(map, "result", -2);
            if (result == 1) {
                SuccessKilled successKilled = successKilledService.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
            } else {
                return new SeckillExecution(seckillId, Objects.requireNonNull(SeckillStatEnum.stateOf(result)));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
        }
    }
}
