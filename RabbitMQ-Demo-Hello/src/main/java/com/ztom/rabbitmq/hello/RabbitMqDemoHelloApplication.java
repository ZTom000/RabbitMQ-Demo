package com.ztom.rabbitmq.hello;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RabbitMqDemoHelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqDemoHelloApplication.class, args);
    }

}
