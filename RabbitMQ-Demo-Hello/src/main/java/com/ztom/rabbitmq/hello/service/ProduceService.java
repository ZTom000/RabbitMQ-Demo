package com.ztom.rabbitmq.hello.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ZTom
 * @date 2021/7/2 11:20
 * @description TODO
 * @since 0.1.0
 */
public interface ProduceService {
    public Boolean sandMessage(String msg);

    public Boolean sandMessages(String[] msgArray);

    public Boolean sandMsgPubSub(String msg) throws IOException, TimeoutException;

    public Boolean sandMsgRouting(String msg, Integer type);

    public Boolean sandMsgTopic(String msg, String routingKey);

    public Boolean sandMsgConfirmAsycn();
}
