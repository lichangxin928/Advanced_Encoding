package com.lcx.consumer;

import com.lcx.utils.RabbitmqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author : lichangxin
 * @create : 2024/5/22 17:07
 * @description
 */
public class ConsumerExchange {


    @Test
    public void ConsumerExchangeTest() {

        try {
            Channel channel = RabbitmqUtils.getChannel();

            //推送的消息如何进行消费的接口回调
            DeliverCallback deliverCallback=(consumerTag, delivery)->{
                String message= new String(delivery.getBody());
                System.out.println(message);
            };
            //取消消费的一个回调接口 如在消费的时候队列被删除掉了
            CancelCallback cancelCallback=(consumerTag)->{
                System.out.println("消息消费被中断");
            };
            channel.basicConsume("exchange_queue1",true,deliverCallback,cancelCallback);
            channel.basicConsume("exchange_queue2",true,deliverCallback,cancelCallback);


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

    }

}
