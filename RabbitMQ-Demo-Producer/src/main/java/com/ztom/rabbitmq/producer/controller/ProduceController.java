package com.ztom.rabbitmq.producer.controller;




import com.ztom.rabbitmq.demo.commons.constant.RabbitMQConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
public class ProduceController {

    private static final Logger logger = LoggerFactory.getLogger(ProduceController.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/confirm")
    public String testConfirm(@RequestParam(name = "msg", defaultValue = "test") String msg) {

        // CorrelationData - 配置信息
        // ack - 是否接收成功
        // cause - 错误信息
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            logger.info("Method Confirm() is running...");
            if (ack) {
                logger.info("消息接收成功！ " + cause);
            } else {
                logger.info("消息接收失败！ " + cause);
            }
        });
        // 发送消息
        rabbitTemplate.convertAndSend("adv_exchange", "test", msg.getBytes(StandardCharsets.UTF_8));
        return "successed";
    }

    /**
     * 回退模式：当消息放松给 Exchange 后， Exchange 路由到 Queue 失败时才会执行 ReturnCallBack
     * 步骤：
     * 1. 开启回退模式： publisher-returns = true
     * 2. 重写 ReturnCallBack 方法
     * 3. 设置 Exchange 处理消息的模式
     * 3.1 如果消息没有路由到 Queue ，则丢弃消息（默认）
     * 3.2 如果消息没有路由到 Queue ，返回给消息发送方 ReturnCallBack
     */
    @GetMapping("/return")
    public String testReturn(@RequestParam(name = "msg", defaultValue = "") String msg) {

        // 设置交换机处理失败消息的模式
        rabbitTemplate.setMandatory(true);

        // 设置 ReturnCallBack
        // message – 消息对象
        // replyCode – 错误码
        // replyText – 错误信息
        // exchange – 交换机名
        // routingKey – 路由键名
        rabbitTemplate.setReturnCallback((Message message, int replyCode, String replyText, String exchange, String routingKey) -> {
            logger.info("return 执行了... ");
            logger.info("{message: " + message + ", replyCode: " + replyCode + ", replyText: "
                    + replyText + ", exchange: " + exchange + ", routingKey: " + routingKey + "}");

        });

        rabbitTemplate.convertAndSend("adv_exchange", "test111", msg.getBytes(StandardCharsets.UTF_8));
        return "successed";
    }

    @GetMapping("/sand")
    public String sandMsgs(@RequestParam(name = "msg", defaultValue = "msg", required = false) String msg) {

        // CorrelationData - 配置信息
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            logger.info("Method Confirm() is running...");
            if (ack) {logger.info("消息接收成功！ " + cause);
            } else {
                logger.info("消息接收失败！ " + cause);
            }
        });

        // 设置交换机处理失败消息的模式
        rabbitTemplate.setMandatory(true);

        // 设置 ReturnCallBack
        rabbitTemplate.setReturnCallback((Message message, int replyCode, String replyText, String exchange, String routingKey) -> {
            logger.info("return 执行了... ");
            logger.info("{message: " + message + ", replyCode: " + replyCode + ", replyText: "
                    + replyText + ", exchange: " + exchange + ", routingKey: " + routingKey + "}");
        });

        for (int i = 0; i < 10; i++) {
            String str = msg + " " + i;
            rabbitTemplate.convertAndSend(RabbitMQConstant.ADV_EXACHANGE_NAME, RabbitMQConstant.ADV_ROUTING_KEY, str.getBytes(StandardCharsets.UTF_8));
        }
        return "successed";
    }

}
