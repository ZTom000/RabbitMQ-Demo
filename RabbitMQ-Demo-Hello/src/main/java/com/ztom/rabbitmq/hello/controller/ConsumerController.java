package com.ztom.rabbitmq.hello.controller;

import com.ztom.rabbitmq.hello.service.ConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author ZTom
 * @date 2021/7/2 11:19
 * @description TODO
 * @since 0.1.0
 */

@RestController
public class ConsumerController {

    private static final Logger log = LoggerFactory.getLogger(ConsumerController.class);

    @Autowired
    private ConsumerService consumerService;

    @GetMapping("/test")
    public String test(){
        log.info(new Date().toString() + " Testing success");
        return "{msg: \"Testing success.\"}";
    }

    @GetMapping("/msg")
    @ResponseBody
    public String getMessage(){
        String result = "failed";
        if(consumerService.getMessage("HELLO")){
            result = "successed";
        }
        return result;
    }
}
