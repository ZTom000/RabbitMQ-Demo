package com.ztom.rabbitmq.hello.util;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author ZTom
 * @date 2021/7/1 23:08
 * @description TODO
 * @since 0.1.0
 */
@Component
@ConfigurationProperties(prefix = "application.yml")
public class RabbitMQUtil {
    private static ConnectionFactory factory;
    private static Connection connection;
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQUtil.class);

    @Value("${spring.rabbitmq.addresses:127.0.0.1}")
    private String addresses;

    @Value("${spring.rabbitmq.port:5672}")
    private int port;

    @Value("${spring.rabbitmq.username:guest}")
    private String username;

    @Value("${spring.rabbitmq.password:guest}")
    private String password;

    public ConnectionFactory init() {
        if (factory == null) {
            // 建立 RabbitMQ 连接工厂
            factory = new ConnectionFactory();
            // 设置 RabbitMQ 地址
            factory.setHost(this.addresses);
            // 设置 RabbitMQ 端口
            factory.setPort(this.port);
            // 设置虚拟机
            // factory.setVirtualHost("/");
            // 设置 RabbitMQ 用户名
            factory.setUsername(this.username);
            // 设置 RabbitMQ 密码
            factory.setPassword(this.password);
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
    public Connection getConnection() throws IOException, TimeoutException {
        if (connection == null) {
            // 创建 RabbitMQ 连接
            connection = init().newConnection();
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
    public Channel getChannel() throws IOException, TimeoutException {

        // 返回一个channel
        return getConnection().createChannel();
    }

    /**
     * @param msg, queueName
     * @return java.lang.Boolean
     * @author ZTom
     * @date 2021/7/5 17:37
     * @description 消息队列生产者方法（往指定消息队列内放消息） 模型：P->[QUEUE]->C
     * @since 0.1.0
     */
    public Boolean sandMessage(String msg, String queueName) {
        Channel channel = null;
        Boolean result;
        try {
            // 获取信道 channel
            channel = getChannel();
            // 设置队列信息
            // queue – 队列名称
            // durable – 如果我们声明一个持久队列（该队列将在服务器重启后继续存在），则为 true
            // exclusive – 如果我们声明一个独占队列（仅限于此连接），则为 true
            // autoDelete – 如果我们声明一个自动删除队列，则为 true（服务器将在不再使用时将其删除）
            // arguments – 队列的其他属性（构造参数）
            channel.queueDeclare(queueName, false, false, false, null);
            // 基础消息发布
            // exchange – 将消息发布到的交换机
            // routingKey – 路由名(队列名称)
            // props – 消息的其他属性 - 路由标头等
            // body – 消息体 (bytes)
            channel.basicPublish("", queueName, null, msg.getBytes());
            logger.info("Send " + msg + "into Hello successed.");
            result = true;
            // 关闭channel
            channel.close();
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            return result;
        }
        return result;
    }

    /**
     * @param queueName
     * @return java.lang.Boolean
     * @author ZTom
     * @date 2021/7/5 17:39
     * @description 从消息队列中获取消息
     * @since 0.1.0
     */
    public Boolean getMessage(String queueName) {
        try {
            Channel channel = getChannel();
            // 接收消息
            DeliverCallback deliverCallback = (consumerTag, message) -> {
                logger.info("消费者接收到了消息: " + new String(message.getBody()));
            };
            // 取消消息时回调
            CancelCallback cancelCallback = (consumerTag) -> {
                logger.info("消费信息被中断...");
            };
            // queue – 队列名称
            // autoAck – 消费成功后是否自动应答， true 为自动应答，false 为不自动应答
            // deliverCallback – 消息送达时的回调方法
            // cancelCallback – 消费者取消时的回调方法
            channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
            // 关闭channel
            channel.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void sandMsgPubSub(String msg) throws IOException, TimeoutException {
        Channel channel = getChannel();

        // 交换机名
        String exchangeName = "test_exchange";

        // exchange – 交换机名
        // type – 交换机类型
        //      1.DIRECT("direct")：定向
        //      2.FANOUT("fanout")：扇形（广播），发送消息到每个消息队列
        //      3.TOPIC("topic")：通配符方式
        //      4.HEADERS("headers")：参数匹配
        // durable – 是否持久化
        // autoDelete – 是否自动删除
        // internal – 是否内部使用
        // arguments – 交换机其他构造参数
        // 创建交换机
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT, true, false, false, null);
        // 队列名
        String queueName[] = {"test_fanout_queue1", "test_fanout_queue2"};
        // 创建消息队列
        channel.queueDeclare(queueName[0], true, false, false, null);
        channel.queueDeclare(queueName[1], true, false, false, null);

        // queue – 队列名
        // exchange – 交换机名
        // routingKey – 绑定规则，路由键
        // 绑定消息队列和交换机
        channel.queueBind(queueName[0], exchangeName, "");
        channel.queueBind(queueName[1], exchangeName, "");

        // 发送消息
        channel.basicPublish(exchangeName, "", null, msg.getBytes());

        // channel.close();
    }

    public Boolean getMsgPubSub(Integer num) {
        try {
            String queueName[] = {"test_fanout_queue1", "test_fanout_queue2"};
            Channel channel = getChannel();
            // 接收消息
            DeliverCallback deliverCallback = (consumerTag, message) -> {
                logger.info("消费者接收到了 " + queueName[num] + " 消息: " + new String(message.getBody()));
            };
            // 取消消息时回调
            CancelCallback cancelCallback = (consumerTag) -> {
                logger.info(queueName[num] + " 消费信息被中断...");
            };
            // queue – 队列名称
            // autoAck – 消费成功后是否自动应答， true 为自动应答，false 为不自动应答
            // deliverCallback – 消息送达时的回调方法
            // cancelCallback – 消费者取消时的回调方法
            channel.basicConsume(queueName[num], true, deliverCallback, cancelCallback);
            // 关闭channel
            // channel.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void sandMsgRouting(String msg, int type) throws IOException, TimeoutException {
        Channel channel = getChannel();

        // 交换机名
        String exchangeName = "test_router";

        // exchange – 交换机名
        // type – 交换机类型
        //      1.DIRECT("direct")：定向
        //      2.FANOUT("fanout")：扇形（广播），发送消息到每个消息队列
        //      3.TOPIC("topic")：通配符方式
        //      4.HEADERS("headers")：参数匹配
        // durable – 是否持久化
        // autoDelete – 是否自动删除
        // internal – 是否内部使用
        // arguments – 交换机其他构造参数
        // 创建交换机
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true, false, false, null);
        // 队列名
        String queueName[] = {"test_direct_queue_1", "test_direct_queue_2"};
        // 创建消息队列
        channel.queueDeclare(queueName[0], true, false, false, null);
        channel.queueDeclare(queueName[1], true, false, false, null);

        String routingKey[] = {"error", "info", "wranning"};
        // queue – 队列名
        // exchange – 交换机名
        // routingKey – 绑定规则，路由键
        // 绑定消息队列和交换机
        channel.queueBind(queueName[0], exchangeName, routingKey[0]);

        channel.queueBind(queueName[1], exchangeName, routingKey[0]);
        channel.queueBind(queueName[1], exchangeName, routingKey[1]);
        channel.queueBind(queueName[1], exchangeName, routingKey[2]);

        msg = msg + " " + routingKey[type];
        // 发送消息
        channel.basicPublish(exchangeName, routingKey[type], null, msg.getBytes());

        // channel.close();
    }

    public Boolean getMsgRouting(Integer num) {
        try {
            logger.info("getMsgRouting running...");
            String queueName[] = {"test_direct_queue_1", "test_direct_queue_2"};
            Channel channel = getChannel();
            // 接收消息
            DeliverCallback deliverCallback = (consumerTag, message) -> {
                logger.info("消费者接收到了 " + queueName[num] + " 消息: " + new String(message.getBody()));
            };
            // 取消消息时回调
            CancelCallback cancelCallback = (consumerTag) -> {
                logger.info(queueName[num] + " 消费信息被中断...");
            };
            // queue – 队列名称
            // autoAck – 消费成功后是否自动应答， true 为自动应答，false 为不自动应答
            // deliverCallback – 消息送达时的回调方法
            // cancelCallback – 消费者取消时的回调方法
            channel.basicConsume(queueName[num], true, deliverCallback, cancelCallback);
            // 关闭channel
            // channel.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void sandMsgTopic(String msg, String routingKey) throws IOException, TimeoutException {
        Channel channel = getChannel();

        // 交换机名
        String exchangeName = "test_topic";

        // exchange – 交换机名
        // type – 交换机类型
        //      1.DIRECT("direct")：定向
        //      2.FANOUT("fanout")：扇形（广播），发送消息到每个消息队列
        //      3.TOPIC("topic")：通配符方式
        //      4.HEADERS("headers")：参数匹配
        // durable – 是否持久化
        // autoDelete – 是否自动删除
        // internal – 是否内部使用
        // arguments – 交换机其他构造参数
        // 创建交换机
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC, true, false, false, null);
        // 队列名
        String queueName[] = {"test_topic_queue_1", "test_topic_queue_2"};
        // 创建消息队列
        channel.queueDeclare(queueName[0], true, false, false, null);
        channel.queueDeclare(queueName[1], true, false, false, null);

        String routingKeys[] = {"*.name.*", "id.#", "age.sex"};
        // queue – 队列名
        // exchange – 交换机名
        // routingKey – 绑定规则，路由键
        // 绑定消息队列和交换机
        channel.queueBind(queueName[0], exchangeName, routingKeys[0]);

        channel.queueBind(queueName[1], exchangeName, routingKeys[1]);
        channel.queueBind(queueName[1], exchangeName, routingKeys[2]);

        msg = msg + " " + routingKey;
        // 发送消息
        channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());

        // channel.close();
    }

    public Boolean getMsgTopic(Integer num) {
        try {
            logger.info("getMsgRouting running...");
            String queueName[] = {"test_topic_queue_1", "test_topic_queue_2"};
            Channel channel = getChannel();
            // 接收消息
            DeliverCallback deliverCallback = (consumerTag, message) -> {
                logger.info("消费者接收到了 " + queueName[num] + " 消息: " + new String(message.getBody()));
            };
            // 取消消息时回调
            CancelCallback cancelCallback = (consumerTag) -> {
                logger.info(queueName[num] + " 消费信息被中断...");
            };
            // queue – 队列名称
            // autoAck – 消费成功后是否自动应答， true 为自动应答，false 为不自动应答
            // deliverCallback – 消息送达时的回调方法
            // cancelCallback – 消费者取消时的回调方法
            channel.basicConsume(queueName[num], true, deliverCallback, cancelCallback);
            // 关闭channel
            // channel.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 异步消息发布确认方法
    public void publishMsgAsycn() throws Exception {
        Channel channel = getChannel();

        // 发送消息总数
        int messageCount = 1000;

        int count = 0;

        // 交换机名
        String exchangeName = "test_async";

        // 创建交换机
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true, false, false, null);
        // 队列名
        String queueName = "asycn_queue";
        String routingKey = "asycn";
        // 创建消息队列
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        // 绑定消息队列和交换机
        channel.queueBind(queueName, exchangeName, routingKey);
        // 创建一个线程安全的有序的哈希表
        ConcurrentSkipListMap<Long, String> oustandingConfirmsMap = new ConcurrentSkipListMap<Long, String>();

        ConfirmCallback ackCallBack = (deliveryTag, multiple) -> {
            if(multiple){
                // 删除已确认的消息
                ConcurrentNavigableMap<Long,String> confrimedMap = oustandingConfirmsMap.headMap(deliveryTag);
                confrimedMap.clear();
            }else{
                oustandingConfirmsMap.remove(deliveryTag);
            }
            logger.info("发送" + deliveryTag + "消息已确认...");

        };

        ConfirmCallback nackCallBack = (deliveryTag, multiple) -> {
            logger.info("发送" + deliveryTag + "消息未确认...");
        };

        // 添加确认监听器，添加成功确认回调方法，添加未成功确认回调方法
        channel.addConfirmListener(ackCallBack, nackCallBack);
        // 开始时间
        long begin = System.currentTimeMillis();
        // 发送信息
        for (int i = 0; i < messageCount; i++) {
            String message = "{message: " + i + "}";
            channel.basicPublish(exchangeName, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
            // 将已发送的消息放入确认消息的map中
            oustandingConfirmsMap.put(channel.getNextPublishSeqNo(), message);
        }
        // 结束时间
        long end = System.currentTimeMillis();

        logger.info("发布" + messageCount + "个异步确认消息,耗时 " + (end - begin) + "ms");

    }
}
