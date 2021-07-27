package com.ztom.rabbit.consumer.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author ZTom
 * @date 2021/7/7 21:14
 * @description TODO
 * @since 0.1.0
 */
@Component
public class RabbitMQListener {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQListener.class);

    // 设置监听器
    @RabbitListener(queues = "boot_queue")
    public void listenerQueue(Message message) {
        logger.info("msg: " + new String(message.getBody()));
    }
}
