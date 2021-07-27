package com.ztom.rabbitmq.dead.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitMqDemoDeadLetterProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqDemoDeadLetterProducerApplication.class, args);
    }

}
