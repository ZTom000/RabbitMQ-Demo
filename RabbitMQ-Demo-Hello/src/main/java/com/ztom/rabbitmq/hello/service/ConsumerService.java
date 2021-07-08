package com.ztom.rabbitmq.hello.service;

/**
 * @author ZTom
 * @date 2021/7/2 11:20
 * @description TODO
 * @since 0.1.0
 */
public interface ConsumerService {
    public Boolean getMessage(String name);

    public Boolean getMsgPubSub(Integer num);

    public Boolean getMsgRouting(Integer num);

    public Boolean getMsgTopic(Integer num);

}
