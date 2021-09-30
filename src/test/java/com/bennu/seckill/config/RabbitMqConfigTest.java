package com.bennu.seckill.config;


import com.rabbitmq.client.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class RabbitMqConfigTest {
    private final static String QUEUE_NAME = "hello";

    @AfterEach
    void tearDown() {

    }

    @Test
    void SenderTest() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("82.157.1.224");
        factory.setUsername("user");
        factory.setPassword("231sdksdfs234");
        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String msg = "Hello World";
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes(StandardCharsets.UTF_8));
            System.out.println("[x] send '" + "msg" + "'");
        }
    }

    @Test
    void ReceiverTest() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("82.157.1.224");
        factory.setUsername("user");
        factory.setPassword("231sdksdfs234");
        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                    String msg = new String(body, StandardCharsets.UTF_8);
                    System.out.println("[x] receive '" + "msg" + "'");
                }
            };
            channel.basicConsume(QUEUE_NAME, true, consumer);
            System.out.println("[x] end ");
        }

    }
}