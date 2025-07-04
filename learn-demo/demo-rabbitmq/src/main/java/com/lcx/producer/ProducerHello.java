package com.lcx.producer;

import com.lcx.utils.RabbitmqUtils;
import com.rabbitmq.client.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.lcx.consumer.ConsumerHello.QUEUE_NAME;


/**
 * @author : lichangxin
 * @create : 2024/5/22 16:19
 * @description
 */
public class ProducerHello {

    @Test
    public void ProducerHelloTest() {
        try {

            Channel channel = RabbitmqUtils.getChannel();

            /**
             * exchangeDeclare
             * exchange：交换器的名称。
             * type：交换器的类型，例如 "direct", "topic", "headers", "fanout" 等。这些类型决定了消息如何路由到队列。
             * durable：是否设置为持久化。如果为true，则交换器会在RabbitMQ服务器重启后仍然存在。默认为false。
             * autoDelete：当所有绑定到此交换器的队列都不再与此交换器绑定时，是否自动删除该交换器。默认为false。
             * arguments：一个可选的参数映射，用于设置交换器的其他属性。不同的交换器类型可能支持不同的参数。
             */


            /**
             * queue：队列的名称。
             * durable：指定队列是否持久化。如果将其设置为 true，则队列将在服务器重启后仍然存在。默认值为 false。
             * exclusive：指定队列是否为独占队列。如果将其设置为 true，则只有创建该队列的连接可以访问它，并且在连接断开时队列将被自动删除。默认值为 false。
             * autoDelete：当最后一个消费者断开连接时，是否自动删除队列。默认值为 false。
             * arguments：一个可选的参数映射，用于设置队列的其他属性。这些参数可以是队列的扩展属性，如消息过期时间（TTL）、死信队列等。
             */
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            System.out.println("交换器创建成功");

            /**
             * exchange：目标交换器的名称。如果为空字符串，则消息将发送到 RabbitMQ 的默认交换器（通常是名为空字符串的直连交换器）。
             * routingKey：路由键。该键用于确定消息应路由到哪个队列。对于不同的交换器类型（如直连、主题、扇出等），路由键的作用方式会有所不同。
             * props：消息属性。这是一个 BasicProperties 对象，用于设置消息的各种属性，如内容类型、内容编码、优先级、持久化标志等。如果不需要设置这些属性，可以传递 null 或使用 MessagePropertiesBuilder 构建一个默认的 BasicProperties 对象。
             * body：消息体。这是一个字节数组，包含要发送的实际消息内容
             */
            channel.basicPublish("",QUEUE_NAME,null,"hello".getBytes());
            System.out.println("消息发送成功");

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

    }



}
