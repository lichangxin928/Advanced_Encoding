package com.lcx.spring.conusmer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * @author : lichangxin
 * @create : 2024/5/27 14:12
 * @description
 */

@Component
@Slf4j
public class SimpleConsumer {

    @RabbitListener(queues = "spring.simple.queue")
    public void receiveD(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到 spring.simple.queue {}", new Date(), msg);
    }
}
