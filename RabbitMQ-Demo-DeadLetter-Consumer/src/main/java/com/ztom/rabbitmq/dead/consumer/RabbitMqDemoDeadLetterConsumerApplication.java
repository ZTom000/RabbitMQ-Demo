package com.ztom.rabbitmq.dead.consumer;

import com.ztom.rabbitmq.dead.consumer.config.IpConfiguration;
import com.ztom.rabbitmq.dead.consumer.service.DeadLetterConsumerService;
import com.ztom.rabbitmq.dead.consumer.thread.DeadLetterThread;
import com.ztom.rabbitmq.dead.consumer.thread.NormalThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class RabbitMqDemoDeadLetterConsumerApplication {



    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RabbitMqDemoDeadLetterConsumerApplication.class, args);
        DeadLetterConsumerService deadLetterConsumerService = context.getBean(DeadLetterConsumerService.class);
        // 将队列绑定死信队列
//        deadLetterConsumerService.getNormalMessage();
        // 测试死信队列是否接收到信息
        deadLetterConsumerService.getDeadLetterMessage();
//        new NormalThread().start();
//        new DeadLetterThread().start();
    }

}
