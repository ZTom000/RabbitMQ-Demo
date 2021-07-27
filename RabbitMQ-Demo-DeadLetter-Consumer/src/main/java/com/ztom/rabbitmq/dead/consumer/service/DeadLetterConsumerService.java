package com.ztom.rabbitmq.dead.consumer.service;


public interface DeadLetterConsumerService {
    public String getNormalMessage();

    public String getDeadLetterMessage();
}
