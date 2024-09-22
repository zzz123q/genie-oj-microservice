package com.zzz123q.genieojbackendquestionservice.message;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.zzz123q.genieojbackendcommon.constant.MessageConstant;
import com.zzz123q.genieojbackendmodel.model.entity.QuestionSubmit;
import com.zzz123q.genieojbackendquestionservice.service.QuestionService;

import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class QuestionMessageManager {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private QuestionService questionService;

    /**
     * 发送消息
     * 
     * @param exchange
     * @param routingKey
     * @param message
     */
    public void sendMessage(String exchange, String routingKey, String message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    /**
     * 指定程序监听的队列和确认机制
     * 
     * @param messgae
     * @param channel
     * @param deliveryTag
     */
    @SneakyThrows
    @RabbitListener(queues = { MessageConstant.SUBMIT_QUEUE_BACKEND }, ackMode = "MANUAL")
    public void receiveMessage(String messgae, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("接收消息: {}", messgae);
        QuestionSubmit questionSubmit = JSONUtil.toBean(messgae, QuestionSubmit.class);
        try {
            questionService.updateQuestionBySubmit(questionSubmit);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, false);
        }
    }
}
