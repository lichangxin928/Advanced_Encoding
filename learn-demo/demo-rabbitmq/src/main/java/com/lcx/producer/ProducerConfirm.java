package com.lcx.producer;

import com.lcx.utils.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author : lichangxin
 * @create : 2024/5/23 11:12
 * @description 发布确认模式生产者
 */
public class ProducerConfirm {


    public static final String QUEUE_NAME = "confirm_queue";

    public static void main(String[] args) {

        singleConfirm();
        batchConfirm();
        syncConfirm();

    }

    /**
     * 单条确认
     */
    public static void singleConfirm() {
        try {
            Channel channel = RabbitmqUtils.getChannel();

            channel.confirmSelect();
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                channel.basicPublish("",QUEUE_NAME,null,(i+"").getBytes());
                channel.waitForConfirms();
            }
            long endTime = System.currentTimeMillis();

            System.out.println("[单条确认]发布10000条消息，耗时："+(endTime-startTime)+"ms");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量确认
     */
    public static void batchConfirm() {

        int batchSize = 100;

        int unConfirmMsgCount = 0;

        try {
            Channel channel = RabbitmqUtils.getChannel();

            channel.confirmSelect();
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);

            long startTime = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                channel.basicPublish("",QUEUE_NAME,null,(i+"").getBytes());
                unConfirmMsgCount++;
                if(batchSize == unConfirmMsgCount) {
                    channel.waitForConfirms();
                    unConfirmMsgCount = 0;
                }
            }
            if(unConfirmMsgCount > 0) {
                channel.waitForConfirms();
            }
            long endTime = System.currentTimeMillis();

            System.out.println("[批量确认]发布10000条消息，耗时："+(endTime-startTime)+"ms");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void syncConfirm() {
        try (Channel channel = RabbitmqUtils.getChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //开启发布确认
            channel.confirmSelect();
            /**
             * 线程安全有序的一个哈希表，适用于高并发的情况
             * 1.轻松的将序号与消息进行关联
             * 2.轻松批量删除条目 只要给到序列号
             * 3.支持并发访问
             */
            ConcurrentSkipListMap<Long, String> outstandingConfirms = new
                    ConcurrentSkipListMap<>();
            /**
             * 确认收到消息的一个回调
             * 1.消息序列号
             * 2.true 可以确认小于等于当前序列号的消息
             * false 确认当前序列号消息
             */
            ConfirmCallback ackCallback = (sequenceNumber, multiple) -> {
                if (multiple) {
                    //返回的是小于等于当前序列号的未确认消息 是一个 map
                    ConcurrentNavigableMap<Long, String> confirmed =
                            outstandingConfirms.headMap(sequenceNumber, true);
                    //清除该部分未确认消息
                    confirmed.clear();
                }else{
                    //只清除当前序列号的消息
                    outstandingConfirms.remove(sequenceNumber);
                }
            };
            ConfirmCallback nackCallback = (sequenceNumber, multiple) -> {
                String message = outstandingConfirms.get(sequenceNumber);
                System.out.println("发布的消息"+message+"未被确认，序列号"+sequenceNumber);
            };
            /**
             * 添加一个异步确认的监听器
             * 1.确认收到消息的回调
             * 2.未收到消息的回调
             */
            channel.addConfirmListener(ackCallback, nackCallback);
            long begin = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                String message = "消息" + i;
                /**
                 * channel.getNextPublishSeqNo()获取下一个消息的序列号
                 * 通过序列号与消息体进行一个关联
                 * 全部都是未确认的消息体
                 */
                outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            }
            long end = System.currentTimeMillis();
            System.out.println("[批量确认]发布10000条消息，耗时：" + (end - begin) +
                    "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
}
