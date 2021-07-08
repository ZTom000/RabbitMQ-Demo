package com.ztom.rabbitmq.producer.controller;

import com.ztom.rabbitmq.producer.config.RabbitMQProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZTom
 * @date 2021/7/7 18:19
 * @description TODO
 * @since 0.1.0
 */
@RestController
public class ProducerController {

    private static final Logger logger = LoggerFactory.getLogger(ProducerController.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/prod")
    public String sandMessage(@RequestParam(name = "msg", defaultValue = "") String msg){

        logger.info("Get msg: " + msg);
        rabbitTemplate.convertAndSend(RabbitMQProducerConfig.EXCHANGE_NAME,"boot.baba", msg);
        return "successed " + msg;
    }
}
