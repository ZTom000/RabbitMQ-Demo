package com.ztom.rabbitmq.dead.producer.service;

public interface DeadLetterProducerService {

    public Boolean sandTTLDeadLetterMessage(String message);
}
