package com.ztom.rabbitmq.consumer.listener;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.ztom.rabbitmq.demo.commons.constant.RabbitMQConstant;

@Component
public class QosListener {

    private static final Logger logger = LoggerFactory.getLogger(QosListener.class);

    private int count = 0;

    @RabbitListener(queues = RabbitMQConstant.ADV_QUEUE_NAME)
    public void onMessage(Channel channel) throws Exception {
        try {
            DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
                try{
                    // 接收转换信息
                    logger.info(new String(message.getBody()));

                    // 处理业务逻辑
                    logger.info("Qos 处理业务逻辑...");

                    // 手动签收
                    channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                }catch (Exception e){
                    channel.basicNack(message.getEnvelope().getDeliveryTag(), false, true);
                }

            };

            CancelCallback cancelCallback = (consumerTag)->{

            };
            channel.basicConsume(RabbitMQConstant.ADV_QUEUE_NAME, false, deliverCallback, cancelCallback);
            logger.info("[X] " + ++count);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
