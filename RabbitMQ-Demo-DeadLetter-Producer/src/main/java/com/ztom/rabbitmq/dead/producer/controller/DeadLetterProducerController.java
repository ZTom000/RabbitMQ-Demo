package com.ztom.rabbitmq.dead.producer.controller;

import com.ztom.rabbitmq.dead.producer.service.DeadLetterProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class DeadLetterProducerController {
    @Autowired
    private DeadLetterProducerService deadLetterProducerService;

    @GetMapping("/dead/sand/{msg}")
    public String sandMsg(@PathVariable("msg") String msg){
        String result = "failed";
        if(deadLetterProducerService.sandTTLDeadLetterMessage(msg)){
            result = "successed";
        }
        return result;
    }
}
