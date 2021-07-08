package com.ztom.rabbitmq.hello.controller;

import com.ztom.rabbitmq.hello.service.ProduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZTom
 * @date 2021/7/6 0:01
 * @description TODO
 * @since 0.1.0
 */
@RestController
public class PubSubController {

    @Autowired
    private ProduceService produceService;

    @GetMapping("/ps")
    public String sandPubSubMsg(@RequestParam(name = "msg",defaultValue = "test") String msg) throws Exception {
        if(produceService.sandMsgPubSub(msg)){
            return "successed";
        }else{
            return "failed";
        }

    }
}
