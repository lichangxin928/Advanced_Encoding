package com.lcx.exchange;

import com.lcx.utils.RabbitmqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author : lichangxin
 * @create : 2024/5/27 11:27
 * @description
 */
public class DeadExchange {


    public static void main(String[] args) throws Exception {
        DeadExchangeTest();
        RabbitmqUtils.CreatConsumer("dead.queue");
    }


    public static void DeadExchangeTest() {

        try {
            Channel channel = RabbitmqUtils.getChannel();


            channel.exchangeDeclare("simple.exchange", BuiltinExchangeType.DIRECT,false);
            channel.queueDeclare("dead.queue",false,false,false,null);
            channel.exchangeDeclare("dead.exchange", BuiltinExchangeType.DIRECT,false);
            channel.queueBind("dead.queue","dead.exchange","dead.key");

            //正常队列绑定死信队列信息
            Map<String, Object> params = new HashMap<>();
            //正常队列设置死信交换机 参数 key 是固定值
            params.put("x-dead-letter-exchange","dead.exchange");
            //正常队列设置死信 routing-key 参数 key 是固定值
            params.put("x-dead-letter-routing-key", "dead.key");
            channel.queueDeclare("simple.queue",false,false,false,params);
            channel.queueBind("simple.queue","simple.exchange","simple.key");


            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
            for (int i = 0; i < 10; i++) {
                channel.basicPublish("simple.exchange","simple.key", properties,"simple 发送消息".getBytes());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
