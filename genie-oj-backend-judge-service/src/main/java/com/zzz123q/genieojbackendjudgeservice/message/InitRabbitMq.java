package com.zzz123q.genieojbackendjudgeservice.message;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.zzz123q.genieojbackendcommon.constant.MessageConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InitRabbitMq {
    public static void doInit() {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(MessageConstant.HOST);
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(MessageConstant.EXCHANGE_NAME, MessageConstant.EXCHANGE_TYPE);

            // 创建队列,随机分配一个队列名称
            channel.queueDeclare(MessageConstant.QUEUE_NAME, true, false, false, null);
            channel.queueBind(MessageConstant.QUEUE_NAME, MessageConstant.EXCHANGE_NAME, MessageConstant.ROUTING_KEY);
            log.info("\n消息队列启动成功!!!\n");
        } catch (Exception e) {
            log.error("\n消息队列启动失败!!!\n");
        }

    }
}
