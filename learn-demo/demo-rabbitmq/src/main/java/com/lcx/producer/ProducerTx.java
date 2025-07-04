package com.lcx.producer;

import com.lcx.utils.RabbitmqUtils;
import com.rabbitmq.client.Channel;

/**
 * @author : lichangxin
 * @create : 2024/5/28 17:57
 * @description
 */
public class ProducerTx {

    public static void main(String[] args) throws Exception {

        ProducerTxTest();
        RabbitmqUtils.CreatConsumer("tx.queue");

    }


    public static void ProducerTxTest() throws Exception {

        Channel channel = RabbitmqUtils.getChannel();
        // 开启事务
        channel.txSelect();
        channel.queueDeclare("tx.queue",false,false,false,null);
        channel.basicPublish("","tx.queue",null,"tx message1".getBytes());
        Thread.sleep(1000);
        channel.basicPublish("","tx.queue",null,"tx message2".getBytes());
        Thread.sleep(2000);
        channel.basicPublish("","tx.queue",null,"tx message3".getBytes());
        channel.txCommit();

    }

}
