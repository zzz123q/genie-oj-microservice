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
            // 创建交换机
            channel.exchangeDeclare(MessageConstant.CODE_EXCHANGE, "direct");
            channel.exchangeDeclare(MessageConstant.SUBMIT_EXCHANGE, "fanout");

            // 创建代码队列
            channel.queueDeclare(MessageConstant.CODE_QUEUE, true, false, false, null);
            channel.queueBind(MessageConstant.CODE_QUEUE, MessageConstant.CODE_EXCHANGE,
                    MessageConstant.CODE_ROUTINGKEY);

            // 创建提交队列
            channel.queueDeclare(MessageConstant.SUBMIT_QUEUE_BACKEND, true, false, false, null);
            channel.queueBind(MessageConstant.SUBMIT_QUEUE_BACKEND, MessageConstant.SUBMIT_EXCHANGE,
                    MessageConstant.SUBMIT_ROUTINGKEY);

            log.info("\n消息队列启动成功!!!\n");
        } catch (Exception e) {
            log.error("\n消息队列启动失败!!!\n");
        }

    }
}
