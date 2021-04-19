/*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/3/25
 *
 * Class name:SecKillReceiver
 */
package com.bennu.seckill.mq;

import com.bennu.seckill.constant.Constant;
import com.bennu.seckill.entity.SuccessKilled;
import com.bennu.seckill.service.SecondKillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 秒杀请求消息接收器
 *
 * @author bennu5
 * @version v1.0
 * @date 2021/3/25
 **/
@Slf4j
@Component
public class SecKillReceiver {

    /**
     * 秒杀服务
     */
    private final SecondKillService secondKillService;

    @Autowired
    public SecKillReceiver(SecondKillService secondKillService) {
        this.secondKillService = secondKillService;
    }

    @RabbitListener(queues = Constant.QUEUE_SEC_KILL)
    public void onRegistrationMessageFromSeckillQueue(SuccessKilled successKilled) {
        log.info("queue {} received registration message: {}", Constant.QUEUE_SEC_KILL, successKilled);
        secondKillService.executeSeckill(successKilled);
    }

    @RabbitListener(queues = Constant.QUEUE_SMS)
    public void onRegistrationMessageFromSmsQueue(SuccessKilled successKilled) {
        log.info("queue {} received registration message: {}", Constant.QUEUE_SMS, successKilled);
        log.info("秒杀成功，发送短信");
    }

    @RabbitListener(queues = Constant.QUEUE_APP)
    public void onLoginMessageFromAppQueue(SuccessKilled killed) {
        log.info("queue {} received message: {}", Constant.QUEUE_APP, killed);
        log.info("APP通知秒杀结果");
    }
}
