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
     * 交换机名称
     */
    String EXCHANGE_NAME = "code_exchange";

    /**
     * 交换机类型
     */
    String EXCHANGE_TYPE = "direct";

    /**
     * 队列名称
     */
    String QUEUE_NAME = "code_queue";

    /**
     * 路由键名称
     */
    String ROUTING_KEY = "my_routingKey";
}
