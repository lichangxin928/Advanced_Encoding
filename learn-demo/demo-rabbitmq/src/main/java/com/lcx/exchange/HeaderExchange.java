package com.lcx.exchange;

import com.lcx.utils.RabbitmqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;


/**
 * @author : lichangxin
 * @create : 2024/5/24 14:00
 * @description
 */
public class HeaderExchange {



    public static void main(String[] args) throws Exception {

        headerExchangeTest();
        RabbitmqUtils.CreatConsumer("queue.headers.a");
        RabbitmqUtils.CreatConsumer("queue.headers.b");
    }

    public static void headerExchangeTest() throws Exception {

        Channel channel = RabbitmqUtils.getChannel();
        channel.confirmSelect();
        channel.exchangeDeclare("exchange.headers", BuiltinExchangeType.HEADERS,false);

        channel.queueDeclare("queue.headers.a", false, false, false, null);
        channel.queueDeclare("queue.headers.b", false, false, false, null);


         // 所有的条件都必须满足
        Map<String, Object> headers1 = new HashMap<>();
        headers1.put("color", "blue");
        headers1.put("size", "large");
        headers1.put("x-match", "all");
        channel.queueBind("queue.headers.a", "exchange.headers", "", headers1);

        Map<String, Object> headers2 = new HashMap<>();
        headers2.put("color", "red");
        headers2.put("size", "small");
        headers2.put("x-match", "any"); // 至少一个条件需要满足

        channel.queueBind("queue.headers.b", "exchange.headers", "", headers2);

        // 发送一个匹配header_queue1的消息
        Map<String, Object> messageHeaders1 = new HashMap<>();
        messageHeaders1.put("color", "blue1");
        messageHeaders1.put("size", "large");
        AMQP.BasicProperties properties1 = new AMQP.BasicProperties.Builder()
                .headers(messageHeaders1)
                .build();
        String messageBody1 = "Message for header_queue1";
        channel.basicPublish("exchange.headers", "", properties1, messageBody1.getBytes());

        // 发送一个匹配header_queue2的消息
        Map<String, Object> messageHeaders2 = new HashMap<>();
        messageHeaders2.put("color", "red");
        AMQP.BasicProperties properties2 = new AMQP.BasicProperties.Builder()
                .headers(messageHeaders2)
                .build();
        String messageBody2 = "Message for header_queue2";
        channel.basicPublish("exchange.headers", "", properties2, messageBody2.getBytes());


    }


}
