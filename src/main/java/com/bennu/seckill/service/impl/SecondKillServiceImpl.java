/*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/1/20
 *
 * Class name:SecondKillServiceImpl
 */
package com.bennu.seckill.service.impl;

import com.bennu.seckill.dto.Exposer;
import com.bennu.seckill.dto.SeckillExecution;
import com.bennu.seckill.dto.SeckillResult;
import com.bennu.seckill.entity.Seckill;
import com.bennu.seckill.entity.SuccessKilled;
import com.bennu.seckill.enums.SeckillStatEnum;
import com.bennu.seckill.exception.RepeatKillException;
import com.bennu.seckill.exception.SeckillCloseException;
import com.bennu.seckill.exception.SeckillException;
import com.bennu.seckill.mq.SecKillSender;
import com.bennu.seckill.service.RedisService;
import com.bennu.seckill.service.SeckillService;
import com.bennu.seckill.service.SecondKillService;
import com.bennu.seckill.service.SuccessKilledService;
import com.bennu.seckill.utils.SeckillUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    /**
     * 秒杀队列发送器
     */
    private final SecKillSender secKillSender;

    private final Lock lock = new ReentrantLock();

    @Autowired
    public SecondKillServiceImpl(SeckillService seckillService, SuccessKilledService successKilledService, RedisService redisService, SecKillSender secKillSender) {
        this.seckillService = seckillService;
        this.successKilledService = successKilledService;
        this.redisService = redisService;
        this.secKillSender = secKillSender;
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
        String md5 = SeckillUtils.getMd5(seckill.getSeckillId());
        return new Exposer(true, md5, seckillId);
    }

    /**
     * 加入盐值混淆，生成MD5，
     *
     * @param seckillId 秒杀ID
     * @return MD5
     */

    @Override
    @Transactional(rollbackFor = SeckillException.class)
    public SeckillExecution executeSeckill(long seckillId, long userPhone) throws SeckillException {
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
    public SeckillResult<SeckillExecution> executeSeckill(SuccessKilled successKilled) {
        SeckillResult<SeckillExecution> result;
        SeckillExecution seckillExecution;
        try {
            seckillExecution = this.executeSeckill(successKilled.getSeckillId(), successKilled.getUserPhone());
            result = new SeckillResult<>(true, seckillExecution);
        } catch (RepeatKillException e1) {
            seckillExecution = new SeckillExecution(successKilled.getSeckillId(), SeckillStatEnum.REPEAT_KILL);
            result = new SeckillResult<>(true, seckillExecution);
        } catch (SeckillCloseException e2) {
            seckillExecution = new SeckillExecution(successKilled.getSeckillId(), SeckillStatEnum.END);
            result = new SeckillResult<>(true, seckillExecution);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            seckillExecution = new SeckillExecution(successKilled.getSeckillId(), SeckillStatEnum.INNER_ERROR);
            result = new SeckillResult<>(true, seckillExecution);
        }
        secKillSender.sendResult(result);
        return result;
    }

    /**
     * 通过Redis执行秒杀操作
     *
     * 整体逻辑
     * （生产者）Web 接到请求后将请求放入RabbitMQ,响应并结束
     * （消费者）从Rabbit MQ 接收到请求后，根据商品ID和用户ID（手机号）查询最近（24小时）内的记录里是否有响应记录
     *  如果有，直接失败；如果无进行处理，存储成功或者失败的记录
     *  问题：如果第一次因为系统问题导致失败，会导致后面一直失败，可以根据交易的具体状态和失败原因进行判断
     * @param seckillId 秒杀Id
     * @param userPhone 手机号
     * @return 秒杀结果
     * @throws SeckillException      秒杀异常
     * @throws RepeatKillException   重复秒杀异常
     * @throws SeckillCloseException 秒杀关闭异常
     */
    private SeckillExecution executeSeckillByRedis(long seckillId, long userPhone) throws SeckillException, RepeatKillException, SeckillCloseException {
        // Redis 处理，通过锁机制进行并发控制

        // 查询缓存，看是否有秒杀记录，
        boolean haveSeckillRecord = checkRecord(seckillId, userPhone);
        // 如果有则直接拒绝，
        if (haveSeckillRecord) {
            return new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
        }
        // 如果无则进行秒杀操作
        SuccessKilled successKilled = new SuccessKilled();
        successKilled.setSeckillId(seckillId);
        successKilled.setUserPhone(userPhone);

        SeckillExecution seckillExecution;

        // ---锁开始位置
        lock.lock();
        try {
            // 先从Redis中获取记录
            Seckill seckill = redisService.getSeckill(seckillId);
            if (seckill == null) {
                // 如果无则访问数据库
                seckill = seckillService.selectByPrimaryKey(seckillId);
            }
            // 如果数据库无记录，或者记录库存小于等于0，则返回秒杀结束，存储秒杀失败记录（Redis）
            if (seckill == null || seckill.getNumber() <= 0) {
                successKilled.setState(SeckillStatEnum.END.getState());
                seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.END, successKilled);
            } else {
                // 否则更新商品信息
                successKilled.setState(SeckillStatEnum.SUCCESS.getState());
                seckill.setNumber(seckill.getNumber() - 1);
                redisService.putSeckill(seckill);
                seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
            }
        } catch (Exception e) {
            seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR, successKilled);
            log.error("executeSeckillByRedis seckill error", e);
        } finally {
            lock.unlock();
        }

        // ---锁结束位置

        // 存储秒杀记录(Redis+数据库)
        redisService.putSeckillRecord("seckillRecord" + seckillId, successKilled);
        successKilledService.saveSuccessKilledRecord(seckillId, userPhone, seckillExecution.getState());
        return seckillExecution;

    }

    private boolean checkRecord(long seckillId, long userPhone) {
        String recordKey = "seckillRecord";
        // 首先判断Redis中是否存在秒杀记录
        Boolean recordNotExist = redisService.checkKey(recordKey);
        // 如果没有，则从数据库中获取并存入Redis
        if (recordNotExist) {
            List<SuccessKilled> list = successKilledService.getAllSuccessKilledRecord();
            Map<String, SuccessKilled> map = new HashMap<>();
            for (SuccessKilled successKilled : list) {
                map.put(successKilled.getSeckillId() + ":" + successKilled.getUserPhone(), successKilled);
            }
            redisService.putSeckillRecord(recordKey, map);
        }
        // 从Redis的Hash结构的记录中判断秒杀记录是否存在
        return redisService.checkRecord(recordKey, seckillId + ":" + userPhone);
    }
}
