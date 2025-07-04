package com.lcx.consumer;

import com.lcx.utils.RabbitmqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author : lichangxin
 * @create : 2024/5/23 9:24
 * @description 消费者 持久化、不公平分发
 */
public class ConsumerDemo {

    public static void main(String[] args) {

        try {
            Channel channel = RabbitmqUtils.getChannel();
            //
            channel.basicQos(1);
            //推送的消息如何进行消费的接口回调
            DeliverCallback deliverCallback=(consumerTag, delivery)->{
                String message= new String(delivery.getBody());
                System.out.println("thread1:" + message);
//                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            };
            //取消消费的一个回调接口 如在消费的时候队列被删除掉了
            CancelCallback cancelCallback=(consumerTag)->{
                System.out.println("消息消费被中断");
            };
            channel.basicConsume("demo_queue1", false,deliverCallback,cancelCallback);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

        new Thread(()->{
            try {
                Channel channel = RabbitmqUtils.getChannel();
                //推送的消息如何进行消费的接口回调
                DeliverCallback deliverCallback=(consumerTag, delivery)->{
                    String message= new String(delivery.getBody());
                    System.out.println("thread2:" + message);
//                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                };
                //取消消费的一个回调接口 如在消费的时候队列被删除掉了
                CancelCallback cancelCallback=(consumerTag)->{
                    System.out.println("消息消费被中断");
                };
                channel.basicConsume("demo_queue1", true,deliverCallback,cancelCallback);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

}
