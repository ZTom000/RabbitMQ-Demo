package com.ztom.rabbitmq.hello.controller;

import com.ztom.rabbitmq.hello.service.ProduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZTom
 * @date 2021/7/7 15:08
 * @description TODO
 * @since 0.1.00.27
 */
@RestController
public class TopicController {
    @Autowired
    private ProduceService produceService;

    @GetMapping("/topic")
    public String sandPubSubMsg(@RequestParam(name = "msg", defaultValue = "test") String msg,
                                @RequestParam(name = "type", defaultValue = "") String routingKey) throws Exception {
        if (produceService.sandMsgTopic(msg, routingKey)) {
            return "successed";
        } else {
            return "failed";
        }
    }
}
