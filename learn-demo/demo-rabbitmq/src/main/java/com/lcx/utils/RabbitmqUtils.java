package com.lcx.utils;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author : lichangxin
 * @create : 2024/5/22 16:39
 * @description
 */
public class RabbitmqUtils {


    public static Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");

        Connection connection = connectionFactory.newConnection();
        return connection.createChannel();
    }

    public static void CreatConsumer(String queueName) throws Exception {

        Channel channel = getChannel();

        channel.basicConsume(queueName,true,(consumerTag,message)->{
            System.out.println("["+queueName+"]接收到的消息："+new String(message.getBody()));
        },consumerTag->{
            System.out.println("["+queueName+"]消息接收失败");
        });

    }


}
