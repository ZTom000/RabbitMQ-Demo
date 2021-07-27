package com.ztom.rabbitmq.demo.commons.utils;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ZTom
 * @date 2021/7/1 23:08
 * @description TODO
 * @since 0.1.0
 */
public class RabbitMQUtil {
    private static ConnectionFactory factory;
    private static Connection connection;
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQUtil.class);


    public ConnectionFactory init(String addresses, int port, String username, String password) {
        if (factory == null) {
            // 建立 RabbitMQ 连接工厂
            factory = new ConnectionFactory();
            // 设置 RabbitMQ 地址
            factory.setHost(addresses);
            // 设置 RabbitMQ 端口
            factory.setPort(port);
            // 设置虚拟机
            // factory.setVirtualHost("/");
            // 设置 RabbitMQ 用户名
            factory.setUsername(username);
            // 设置 RabbitMQ 密码
            factory.setPassword(password);
            logger.info("ConnectionFactory finished init...");
        }
        return factory;
    }

    /**
     * @param
     * @return java.sql.Connection
     * @author ZTom
     * @date 2021/7/1 23:29
     * @description 获取RabbitMQ队列连接
     * @since 0.1.0
     */
    public Connection getConnection(String addresses, int port, String username, String password) throws IOException, TimeoutException {
        if (connection == null) {
            // 创建 RabbitMQ 连接
            connection = init(addresses, port, username,  password).newConnection();
            // connection = factory.newConnection();
        }
        return connection;
    }

    /**
     * @param
     * @return com.rabbitmq.client.Channel
     * @author ZTom
     * @date 2021/7/2 0:03
     * @description 获取 RabbitMQ Channel
     * @since 0.1.0
     */
    public Channel getChannel(String addresses, int port, String username, String password) throws IOException, TimeoutException {

        // 返回一个channel
        return getConnection(addresses, port, username,  password).createChannel();
    }

}
