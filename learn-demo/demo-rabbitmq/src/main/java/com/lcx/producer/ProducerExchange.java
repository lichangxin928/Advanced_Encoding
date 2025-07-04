package com.lcx.producer;

import com.lcx.utils.RabbitmqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author : lichangxin
 * @create : 2024/5/22 17:00
 * @description
 */
public class ProducerExchange {


    @Test
    public void ProducerExchangeTest() {

        try {
            Channel channel = RabbitmqUtils.getChannel();

            /**
             * exchange：交换器的名称。
             * type：交换器的类型，例如 "direct", "topic", "headers", "fanout" 等。这些类型决定了消息如何路由到队列。
             * durable：是否设置为持久化。如果为true，则交换器会在RabbitMQ服务器重启后仍然存在。默认为false。
             * autoDelete：当所有绑定到此交换器的队列都不再与此交换器绑定时，是否自动删除该交换器。默认为false。
             * arguments：一个可选的参数映射，用于设置交换器的其他属性。不同的交换器类型可能支持不同的参数。
             */
            channel.exchangeDeclare("my_exchange1", BuiltinExchangeType.DIRECT,false,true,null);
//            channel.exchangeDeclare("my_exchange2", BuiltinExchangeType.DIRECT);
            channel.queueDeclare("exchange_queue1",false,false,true,null);
            channel.queueDeclare("exchange_queue2",false,false,true,null);

            /**
             * queue：队列的名称，表示要将哪个队列绑定到交换器。
             * exchange：交换器的名称，表示要将队列绑定到哪个交换器。
             * routingKey：路由键，是一个字符串，用于决定如何将消息从交换器路由到队列。当生产者发送消息到交换器时，会指定一个路由键，RabbitMQ 会根据这个路由键和队列与交换器之间的绑定关系，将消息路由到相应的队列。
             */
            channel.queueBind("exchange_queue1", "my_exchange1", "my_routing_key", null);
            channel.queueBind("exchange_queue2", "my_exchange1", "my_routing_key", null);
            channel.basicPublish("my_exchange1", "my_routing_key", null, "hello".getBytes());
            channel.basicPublish("", "exchange_queue1", null, "hello".getBytes());


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

    }

}
