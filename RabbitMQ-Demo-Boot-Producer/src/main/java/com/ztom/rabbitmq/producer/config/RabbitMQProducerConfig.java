package com.ztom.rabbitmq.producer.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZTom
 * @date 2021/7/7 18:00
 * @description TODO
 * @since 0.1.0
 */
@Configuration
public class RabbitMQProducerConfig {
    public static final String EXCHANGE_NAME = "boot_topic_exchange";
    public static final String QUEUE_NAME = "boot_queue";

    // 设置交换机
    @Bean("bootExchange")
    public Exchange initExchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    // 设置Queue队列
    @Bean("bootQueue")
    public Queue initQueue(){
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    // 队列与交换机的绑定关系 Binding
    @Bean // 必须加 @Bean 注解，使得 Spring 在初始化是执行该方法将 Binding 注入IOC容器中，创建交换机与队列的绑定关系
    public Binding bindingQueueExchange(@Qualifier("bootQueue") Queue queue, @Qualifier("bootExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("boot.#").noargs();
    }
}
