package com.lcx.producer;

import com.lcx.utils.RabbitmqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;


/**
 * @author : lichangxin
 * @create : 2024/5/23 14:20
 * @description
 */
public class ProducerDurable {
    private static final String QUEUE_NAME = "my_persistent_queue";
    private static final String EXCHANGE_NAME = "my_persistent_exchange";

    public static void main(String[] args) throws Exception {

        try (Channel channel = RabbitmqUtils.getChannel()) {

            // 声明一个持久的、直连类型的交换机
            channel.exchangeDeclare(EXCHANGE_NAME, "direct", true); // 第三个参数为true表示交换机持久化

            // 声明一个持久的队列
            channel.queueDeclare(QUEUE_NAME, true, false, false, null); // 第二个参数为true表示队列持久化

            // 绑定队列到交换机上
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "my_routing_key");

            // 发送持久化的消息
            String message = "Hello, RabbitMQ Persistent Messaging!";
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2) // 设置消息的delivery mode为2，表示消息持久化
                    .build();
            channel.basicPublish(EXCHANGE_NAME, "my_routing_key", properties, message.getBytes("UTF-8"));

            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
