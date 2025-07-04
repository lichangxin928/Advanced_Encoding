package com.lcx.exchange;

import com.lcx.utils.RabbitmqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

/**
 * @author : lichangxin
 * @create : 2024/5/24 11:47
 * @description
 */
public class TopicExchange {
    public static void main(String[] args) throws Exception{

        TopicExchangeTest();
        RabbitmqUtils.CreatConsumer("topic_queue1");
        RabbitmqUtils.CreatConsumer("topic_queue2");

    }


    public static void TopicExchangeTest() throws Exception{

        Channel channel = RabbitmqUtils.getChannel();

        channel.exchangeDeclare("topic_exchange", BuiltinExchangeType.TOPIC ,false);
        channel.queueDeclare("topic_queue1",false,false,false,null);
        channel.queueDeclare("topic_queue2",false,false,false,null);
        // TOPIC 类型的交换机，能够匹配通配符
        /**
         * 发送到类型是 topic 交换机的消息的 routing_key 不能随意写，必须满足一定的要求，它必须是一个单
         * 词列表，以点号分隔开。这些单词可以是任意单词，比如说："stock.usd.nyse", "nyse.vmw",
         * "quick.orange.rabbit".这种类型的。当然这个单词列表最多不能超过 255 个字节。
         */
        channel.queueBind("topic_queue1","topic_exchange","topic.#");
        channel.queueBind("topic_queue2","topic_exchange","topic.routing2");
        channel.basicPublish("topic_exchange","topic.routing2",null,"topic_routing".getBytes());


    }
}
