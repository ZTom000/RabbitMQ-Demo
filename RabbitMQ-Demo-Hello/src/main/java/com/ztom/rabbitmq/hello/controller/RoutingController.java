package com.ztom.rabbitmq.hello.controller;

import com.ztom.rabbitmq.hello.service.ProduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZTom
 * @date 2021/7/6 15:43
 * @description TODO
 * @since 0.1.0
 */
@RestController
public class RoutingController {

    @Autowired
    private ProduceService produceService;

    @GetMapping("/router")
    public String sandPubSubMsg(@RequestParam(name = "msg", defaultValue = "test") String msg,
                                @RequestParam(name = "type", defaultValue = "0") int type) throws Exception {
        if (produceService.sandMsgRouting(msg, type)) {
            return "successed";
        } else {
            return "failed";
        }
    }
}
