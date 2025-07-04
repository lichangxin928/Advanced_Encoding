package com.lcx.spring.conusmer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author : lichangxin
 * @create : 2024/5/27 15:56
 * @description
 */

@Component
@Slf4j
public class DelayConsumer {

    @RabbitListener(queues = "spring.ttl.queue")
    public void receiveD(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到 spring.ttl.queue {}", new Date(), msg);
    }


    @RabbitListener(queues = "spring.delay.queue1")
    public void receiveDelay(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到 spring.delay.queue1 {}", new Date(), msg);
    }



}
