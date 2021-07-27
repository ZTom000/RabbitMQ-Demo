package com.ztom.rabbitmq.dead.consumer.thread;

import com.ztom.rabbitmq.dead.consumer.service.DeadLetterConsumerService;
import org.springframework.beans.factory.annotation.Autowired;

public class NormalThread extends Thread{

    @Autowired
    private DeadLetterConsumerService deadLetterConsumerService;

    @Override
    public void run() {
        deadLetterConsumerService.getNormalMessage();
    }
}
