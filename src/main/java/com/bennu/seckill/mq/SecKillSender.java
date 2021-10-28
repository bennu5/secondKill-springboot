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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public SecKillSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(SuccessKilled successKilled) {
        log.info("Send msg = {}", successKilled);

        // 发送确认修改
        /*
         * rabbitmq 整个消息投递的路径为：
         * producer->rabbitmq broker cluster->exchange->queue->consumer
         * message 从 producer 到 rabbitmq broker cluster 则会返回一个 confirmCallback
         * message 从 exchange->queue 投递失败则会返回一个 returnCallback 。
         * 利用这两个 callback 控制消息的最终一致性和部分纠错能力
         */
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                assert correlationData != null;
                log.error("Send seckill message error id = {}", correlationData.getId());
            }
        });

        rabbitTemplate.setReturnsCallback(returnedMessage -> log.error("Return message = {}", returnedMessage));


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
