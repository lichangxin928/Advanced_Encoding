package com.lcx.exchange;

import com.lcx.utils.RabbitmqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;


/**
 * @author : lichangxin
 * @create : 2024/5/23 14:50
 * @description
 */
public class DirectExchange {

    public static void main(String[] args) throws Exception{

//        DirectExchangeTest();
        DirectExchangeTest();
        RabbitmqUtils.CreatConsumer("direct_queue");

    }


    public static void DirectExchangeTest() throws Exception{

        Channel channel = RabbitmqUtils.getChannel();

        channel.exchangeDeclare("direct_exchange", BuiltinExchangeType.DIRECT,false);
        channel.queueDeclare("direct_queue",false,false,false,null);
//        channel.queueBind("direct_queue","direct_exchange","direct_routing");
        // direct 类型的交换机，不会识别routing key 通配符
        channel.queueBind("direct_queue","direct_exchange","direct_routing2#");
//        channel.queueBind("direct_queue","direct_exchange","direct_routing21");
        channel.basicPublish("direct_exchange","direct_routing21",null,"direct_routing".getBytes());


    }


}
