package com.ztom.rabbitmq.consumer.listener;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * Consumer Ack机制:
 * 1. 设置手动签收 acknowledge = "manual"
 * 2. 让监听器类实现 ChannelAwareMessageListener 接口
 * 3. 如果消息成功处理，则调用 Channel 的 basicAck() 签收
 * 4. 如果消息处理失败，则调用 Channel 的 basicNack() 拒绝签收， broker 重新发送给 consumer
 */
@Component
public class AckListener implements ChannelAwareMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(AckListener.class);

    @Override
//    @RabbitListener(queues = "adv_queue")
    public void onMessage(Message message, Channel channel) throws Exception {
        // 获取 Message 的 deliveryTag
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // 接收转换信息
            logger.info(new String(message.getBody()));
            // 处理业务逻辑
            logger.info("处理业务逻辑...");
            // int i = 3 / 0;
            // 手动签收
            channel.basicAck(deliveryTag, true);

        } catch (Exception e) {
            e.printStackTrace();
            channel.basicNack(deliveryTag, true, true);
        }
    }
}
