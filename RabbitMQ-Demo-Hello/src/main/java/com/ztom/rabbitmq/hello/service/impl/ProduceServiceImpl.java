package com.ztom.rabbitmq.hello.service.impl;

import com.ztom.rabbitmq.hello.service.ProduceService;
import com.ztom.rabbitmq.hello.util.RabbitMQUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ZTom
 * @date 2021/7/2 11:21
 * @description TODO
 * @since 0.1.0
 */
@Service
public class ProduceServiceImpl implements ProduceService {

    @Autowired
    private RabbitMQUtil rabbitMQUtil;

    @Override
    public Boolean sandMessage(String msg) {
        return rabbitMQUtil.sandMessage(msg, "HELLO");
    }

    @Override
    public Boolean sandMessages(String[] msgArray) {
        boolean flag = false;
        for(String msg : msgArray){
            flag = rabbitMQUtil.sandMessage(msg, "HELLO");
        }
        return flag;
    }

    @Override
    public Boolean sandMsgPubSub(String msg) {
        Boolean flag = false;
        try {
            rabbitMQUtil.sandMsgPubSub(msg);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public Boolean sandMsgRouting(String msg, Integer type) {
        Boolean flag = false;
        try {
            rabbitMQUtil.sandMsgRouting(msg, type);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public Boolean sandMsgTopic(String msg, String routingKey) {
        Boolean flag = false;
        try {
            rabbitMQUtil.sandMsgTopic(msg, routingKey);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public Boolean sandMsgConfirmAsycn()  {
        try{
            rabbitMQUtil.publishMsgAsycn();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}
