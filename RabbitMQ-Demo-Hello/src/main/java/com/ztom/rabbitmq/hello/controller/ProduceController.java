package com.ztom.rabbitmq.hello.controller;

import com.ztom.rabbitmq.hello.service.ProduceService;
import com.ztom.rabbitmq.hello.util.RabbitMQUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZTom
 * @date 2021/7/1 23:06
 * @description TODO
 * @since 0.1.0
 */
@RestController
public class ProduceController {

    @Autowired
    private ProduceService produceService;

    @GetMapping("/sand")
    public String sendHello(@RequestParam(name = "msg", defaultValue = "hello RabbitMQ") String msg){
        String result = "failed";
        if(produceService.sandMessage(msg)) {
            result = "successed";
        }
        return result;
    }

}
