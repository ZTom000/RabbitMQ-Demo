package com.ztom.rabbitmq.dead.producer.service.impl;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.ztom.rabbitmq.dead.producer.service.DeadLetterProducerService;
import com.ztom.rabbitmq.demo.commons.constant.RabbitMQConstant;
import com.ztom.rabbitmq.demo.commons.utils.RabbitMQUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Service
@ConfigurationProperties(prefix = "application.yml")
public class DeadLetterProducerServiceImpl implements DeadLetterProducerService {

    private static final Logger logger = LoggerFactory.getLogger(DeadLetterProducerServiceImpl.class);

    @Value("${spring.rabbitmq.addresses:127.0.0.1}")
    private String addresses;

    @Value("${spring.rabbitmq.port:5672}")
    private int port;

    @Value("${spring.rabbitmq.username:guest}")
    private String username;

    @Value("${spring.rabbitmq.password:guest}")
    private String password;

    @Override
    public Boolean sandTTLDeadLetterMessage(String message) {
        Boolean flag = false;
        RabbitMQUtil rabbitMQUtil = new RabbitMQUtil();
        try {
            logger.info("{addresses: \"" + addresses + "\", port: \"" + port + "\", username: \"" + username +"\", password: \""+ password + "\"}");
            Channel channel =  rabbitMQUtil.getChannel(addresses, port, username, password);
            channel.exchangeDeclare(RabbitMQConstant.NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
            // 给消息添加一个TTL时间，到期后还未消费，消息才进入到死信队列
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("5000").build();
            channel.basicPublish(RabbitMQConstant.NORMAL_EXCHANGE, RabbitMQConstant.NORMAL_ROUTER_KEY, properties, message.getBytes(StandardCharsets.UTF_8));
            flag = true;
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
