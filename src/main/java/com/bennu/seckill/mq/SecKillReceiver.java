/*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/3/25
 *
 * Class name:SecKillReceiver
 */
package com.bennu.seckill.mq;

import com.bennu.seckill.constant.Constant;
import com.bennu.seckill.dto.SeckillExecution;
import com.bennu.seckill.dto.SeckillResult;
import com.bennu.seckill.entity.SuccessKilled;
import com.bennu.seckill.enums.RabbitMqAction;
import com.bennu.seckill.enums.SeckillStatEnum;
import com.bennu.seckill.service.SecondKillService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

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

//    @RabbitListener(queues = Constant.QUEUE_SEC_KILL)
//    public void onRegistrationMessageFromSeckillQueue(SuccessKilled successKilled) {
//        log.info("queue {} received registration message: {}", Constant.QUEUE_SEC_KILL, successKilled);
//        secondKillService.executeSeckill(successKilled);
//    }

    @RabbitListener(queues = Constant.QUEUE_SEC_KILL)
    @RabbitHandler
    public void processMessageFromSeckillQueue(String content, Message message, Channel channel) {
        log.info("queue {} received registration message: {}", Constant.QUEUE_SEC_KILL, content);
        RabbitMqAction action = RabbitMqAction.ACCATP;
        try {
            log.info("execute job");
            SeckillResult<SeckillExecution> result = secondKillService.executeSeckill(new SuccessKilled());
            if (result.isSuccess()) {
                if (result.getData().getState() == SeckillStatEnum.INNER_ERROR.getState()) {
                    action = RabbitMqAction.RETRY;
                }
            } else {
                action = RabbitMqAction.REJECT;
            }
        } catch (Exception e) {
            action = RabbitMqAction.REJECT;
        } finally {
            try {
                switch (action) {
                    case ACCATP:
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                        break;
                    case RETRY:
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                        break;
                    case REJECT:
                        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
                        break;
                    default:
                        log.error("error : unknown rabbitMQ action");
                }
            } catch (IOException e) {
                log.error("error: ack or nack error", e);
            }

        }
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
