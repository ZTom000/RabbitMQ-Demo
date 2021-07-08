package com.ztom.rabbitmq.producer.config;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZTom
 * @date 2021/7/8 15:08
 * @description TODO
 * @since 0.1.0
 */
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "adv_exchange";
    public static final String QUEUE_NAME = "adv_queue";
    public static final String ROUTING_KEY = "test";

    @Bean("advExchange")
    public Exchange getExchange(){
        return ExchangeBuilder.directExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean("advQueue")
    public Queue getQueue(){
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean
    public Binding bindingQueueExchange(@Qualifier("advQueue") Queue queue, @Qualifier("advExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY).noargs();
    }
}
