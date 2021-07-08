package com.ztom.rabbitmq.hello.controller;

import com.ztom.rabbitmq.hello.service.ProduceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


/**
 * @author ZTom
 * @date 2021/7/4 17:50
 * @description TODO
 * @since 0.1.0
 */
@RestController
public class WorkQueueController {

    private static final Logger logger = LoggerFactory.getLogger(WorkQueueController.class);

    @Autowired
    private ProduceService produceService;

    @GetMapping("/wq")
    public String testWorkQueue() {
        int length = 10;
        String result = "failed";
        String msgArray[] = new String[length];
        for (int i = 0; i < length; i++) {
            msgArray[i] = new Date() + "发送的信息";
        }
        produceService.sandMessages(msgArray);
        result = "successed";
        return result;
    }
}
