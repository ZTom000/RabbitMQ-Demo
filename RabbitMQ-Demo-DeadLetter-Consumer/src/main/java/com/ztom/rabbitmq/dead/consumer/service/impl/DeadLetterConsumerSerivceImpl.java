package com.ztom.rabbitmq.dead.consumer.service.impl;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ztom.rabbitmq.dead.consumer.service.DeadLetterConsumerService;
import com.ztom.rabbitmq.demo.commons.constant.RabbitMQConstant;
import com.ztom.rabbitmq.demo.commons.utils.RabbitMQUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
@ConfigurationProperties(prefix = "application.yml")
public class DeadLetterConsumerSerivceImpl implements DeadLetterConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(DeadLetterConsumerSerivceImpl.class);

    @Value("${spring.rabbitmq.addresses:127.0.0.1}")
    private String addresses;

    @Value("${spring.rabbitmq.port:5672}")
    private int port;

    @Value("${spring.rabbitmq.username:guest}")
    private String username;

    @Value("${spring.rabbitmq.password:guest}")
    private String password;

    // 获取普通队列消息
    @Override
    public String getNormalMessage() {
        RabbitMQUtil rabbitMQUtil = new RabbitMQUtil();
        try {
            Channel channel = rabbitMQUtil.getChannel(addresses, port, username, password);

            // 声明一个死信队列
            channel.queueDeclare(RabbitMQConstant.DEAD_QUEUE, false, false, false, null);
            // 声明一个死信交换机
            channel.exchangeDeclare(RabbitMQConstant.DEAD_EXCHANGE,BuiltinExchangeType.DIRECT);
            // 绑定死信队列与死信交换机
            channel.queueBind(RabbitMQConstant.DEAD_QUEUE, RabbitMQConstant.DEAD_EXCHANGE,RabbitMQConstant.DEAD_ROUTER_KEY);

            // 绑定正常队列与死信交换机的绑定关系
            Map<String, Object> deadLetterParams = new HashMap<String, Object>();
            deadLetterParams.put("x-dead-letter-exchange", RabbitMQConstant.DEAD_EXCHANGE);
            deadLetterParams.put("x-dead-letter-routing-key", RabbitMQConstant.DEAD_ROUTER_KEY);

            // 声明一个正常队列
            channel.queueDeclare(RabbitMQConstant.NORMAL_QUEUE, false, false, false, deadLetterParams);
            // 声明一个正常交换机
            channel.exchangeDeclare(RabbitMQConstant.NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
            // 绑定正常交换机与正常队列
            channel.queueBind(RabbitMQConstant.NORMAL_QUEUE, RabbitMQConstant.NORMAL_EXCHANGE, RabbitMQConstant.NORMAL_ROUTER_KEY);

            // 接收消息
            DeliverCallback deliverCallback = (consumerTag, message) -> {
                logger.info("消费者接收到了消息: " + new String(message.getBody()));
            };
            // 取消消息时回调
            CancelCallback cancelCallback = (consumerTag) -> {
                logger.info("消费信息被中断...");
            };
            // queue – 队列名称
            // autoAck – 消费成功后是否自动应答， true 为自动应答，false 为不自动应答
            // deliverCallback – 消息送达时的回调方法
            // cancelCallback – 消费者取消时的回调方法
            channel.basicConsume(RabbitMQConstant.NORMAL_QUEUE, true, deliverCallback, cancelCallback);
            // 关闭channel
            //channel.close();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getDeadLetterMessage() {
        RabbitMQUtil rabbitMQUtil = new RabbitMQUtil();

        try {
            Channel channel = rabbitMQUtil.getChannel(addresses, port, username, password);
            // 接收消息
            DeliverCallback deliverCallback = (consumerTag, message) -> {
                logger.info("消费者接收到了死信队列的消息: " + new String(message.getBody()));
            };
            // 取消消息时回调
            CancelCallback cancelCallback = (consumerTag) -> {
                logger.info("消费死信信息被中断...");
            };
            channel.basicConsume(RabbitMQConstant.DEAD_QUEUE, true, deliverCallback, cancelCallback);
            // 关闭channel
            //channel.close();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }
}
