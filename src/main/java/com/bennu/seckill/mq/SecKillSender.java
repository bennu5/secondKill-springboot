/*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/3/25
 *
 * Class name:SecKillSender
 */
package com.bennu.seckill.mq;

import com.bennu.seckill.dto.SeckillExecution;
import com.bennu.seckill.dto.SeckillResult;
import com.bennu.seckill.entity.SuccessKilled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 功能描述
 *
 * @author bennu5
 * @version v1.0
 * @date 2021/3/25
 **/
@Slf4j
@Service
public class SecKillSender {
    private final AmqpTemplate rabbitTemplate;

    @Autowired
    public SecKillSender(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(SuccessKilled successKilled) {
        log.info("Send msg = {}", successKilled);
        rabbitTemplate.convertAndSend("seckill", "", successKilled);
    }

    public void sendResult(SeckillResult<SeckillExecution> result) {
//        if (result.isSuccess()) {
//            rabbitTemplate.convertAndSend("seckillResult", "success", result);
//        } else {
//            rabbitTemplate.convertAndSend("seckillResult", "failed", result);
//        }
    }
}
