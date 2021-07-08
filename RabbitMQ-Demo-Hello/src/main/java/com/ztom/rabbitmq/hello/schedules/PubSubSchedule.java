package com.ztom.rabbitmq.hello.schedules;

import com.ztom.rabbitmq.hello.service.ConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author ZTom
 * @date 2021/7/5 23:41
 * @description TODO
 * @since 0.1.0
 */
//@Component
public class PubSubSchedule {

    private static final Logger logger = LoggerFactory.getLogger(PubSubSchedule.class);

    @Value("${server.port}")
    private Integer port;

    @Autowired
    private ConsumerService consumerService;

//    @Scheduled(cron = "5 * * * * ?")
//    public void getPubSubSchedule(){
//        logger.info("Schedule " + port % 2 + " is running...");
//        consumerService.getMsgPubSub(port % 2);
//    }
}
