package com.lcx.producer;

import com.lcx.utils.RabbitmqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : lichangxin
 * @create : 2024/5/27 13:49
 * @description
 */
public class ProducerDelay {


    public static void main(String[] args) throws Exception {
        ProducerDelayTest();
        RabbitmqUtils.CreatConsumer("delay.dead.queue");
    }

    public static void ProducerDelayTest() throws Exception{

        Channel channel = RabbitmqUtils.getChannel();

        Map<String,Object> delayQueueParam = new HashMap<>();
        delayQueueParam.put("x-message-ttl",10000);
        delayQueueParam.put("x-dead-letter-exchange","delay.dead.exchange");
        delayQueueParam.put("x-dead-letter-routing-key","delay.dead.key");

        channel.queueDeclare("delay.queue",false,false,false,delayQueueParam);
        channel.exchangeDeclare("delay.exchange", BuiltinExchangeType.DIRECT);
        channel.queueBind("delay.queue","delay.exchange","delay.key");

        channel.queueDeclare("delay.dead.queue",false,false,false,null);
        channel.exchangeDeclare("delay.dead.exchange",BuiltinExchangeType.DIRECT);
        channel.queueBind("delay.dead.queue","delay.dead.exchange","delay.dead.key");

        channel.basicPublish("delay.exchange","delay.key",null,"delay message".getBytes());


    }
}
