package com.lcx.exchange;

import com.lcx.utils.RabbitmqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

/**
 * @author : lichangxin
 * @create : 2024/5/24 11:44
 * @description
 */
public class FanoutExchange {

    public static void main(String[] args) throws Exception{

        FanoutExchangeTest();
        RabbitmqUtils.CreatConsumer("fanout_queue");

    }


    public static void FanoutExchangeTest() throws Exception{

        Channel channel = RabbitmqUtils.getChannel();

        channel.exchangeDeclare("fanout_exchange", BuiltinExchangeType.FANOUT,false);
        channel.queueDeclare("fanout_queue",false,false,false,null);
        // FANOUT 类型的交换机，与 routing key 无关，会向 exchange 上的所有队列发送消息
        channel.queueBind("fanout_queue","fanout_exchange","fanout_routing1312331232");
        channel.basicPublish("fanout_exchange","fanout_routing21222",null,"fanout_routing".getBytes());


    }
}
