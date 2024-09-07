package com.zzz123q.genieojbackendjudgeservice.message;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.zzz123q.genieojbackendcommon.constant.MessageConstant;
import com.zzz123q.genieojbackendjudgeservice.judge.JudgeService;
import com.zzz123q.genieojbackendserviceclient.service.QuestionFeignClient;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MessageConsumer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private JudgeService judgeService;

    @Resource
    private QuestionFeignClient questionFeignClient;

    /**
     * 指定程序监听的队列和确认机制
     * 
     * @param messgae
     * @param channel
     * @param deliveryTag
     */
    @SneakyThrows
    @RabbitListener(queues = { MessageConstant.QUEUE_NAME }, ackMode = "MANUAL")
    public void receiveMessage(String messgae, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("接收消息: {}", messgae);
        long questionSubmitId = Long.parseLong(messgae);
        try {
            judgeService.doJudge(questionSubmitId);
            questionFeignClient.updateQuestionBySubmitId(questionSubmitId);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, true);
        }
    }
}
