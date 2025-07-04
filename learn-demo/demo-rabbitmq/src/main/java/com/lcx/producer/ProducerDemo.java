package com.lcx.producer;

import com.lcx.utils.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author : lichangxin
 * @create : 2024/5/23 9:24
 * @description 生产者 持久化、不公平分发
 */
public class ProducerDemo {

    public static void main(String[] args) {

        try {
            Channel channel = RabbitmqUtils.getChannel();
            channel.exchangeDeclare("demo_exchange1", "direct", false,true,null);
            channel.queueDeclare("demo_queue1",false,false,false,null);
            channel.queueBind("demo_queue1","demo_exchange1","demo_routingkey1");
            while (true) {
                Scanner input = new Scanner(System.in);
                channel.basicPublish("demo_exchange1","demo_routingkey1", MessageProperties.PERSISTENT_TEXT_PLAIN,input.nextLine().getBytes());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

    }

}
