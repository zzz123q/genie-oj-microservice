package com.zzz123q.genieojbackendcommon.constant;

/**
 * 消息队列常量
 */
public interface MessageConstant {

    /**
     * 消息队列地址
     */
    String HOST = "localhost";

    /**
     * 判题队列交换机
     */
    String CODE_EXCHANGE = "code_exchange";

    /**
     * 判题队列名称
     */
    String CODE_QUEUE = "code_queue";

    /**
     * 判题队列路由键名称
     */
    String CODE_ROUTINGKEY = "code_routingKey";

    /**
     * 提交队列交换机
     */
    String SUBMIT_EXCHANGE = "submit_exchange";

    /**
     * 前端提交队列名称
     */
    String SUBMIT_QUEUE_FRONTEND = "submit_queue_frontend";

    /**
     * 后端提交队列名称
     */
    String SUBMIT_QUEUE_BACKEND = "submit_queue_backend";

    /**
     * 提交队列路由键名称(在订阅模式中无意义)
     */
    String SUBMIT_ROUTINGKEY = "submit_routingKey";
}
