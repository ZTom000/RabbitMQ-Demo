package com.ztom.rabbitmq.hello.service.impl;

import com.ztom.rabbitmq.hello.service.ConsumerService;
import com.ztom.rabbitmq.hello.util.RabbitMQUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZTom
 * @date 2021/7/2 11:21
 * @description TODO
 * @since 0.1.0
 */

@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Autowired
    private RabbitMQUtil rabbitMQUtil;

    @Override
    public Boolean getMessage(String name) {
        return rabbitMQUtil.getMessage(name);
    }

    @Override
    public Boolean getMsgPubSub(Integer num) {
        return rabbitMQUtil.getMsgPubSub(num);
    }

    @Override
    public Boolean getMsgRouting(Integer num) {
        return rabbitMQUtil.getMsgRouting(num);
    }

    @Override
    public Boolean getMsgTopic(Integer num) {
        return rabbitMQUtil.getMsgTopic(num);
    }
}
