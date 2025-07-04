package com.lcx.spring.conusmer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.lcx.spring.config.MQConfirmConfig.BACKUP_QUEUE_NAME;

/**
 * @author : lichangxin
 * @create : 2024/5/30 10:40
 * @description
 */

@Component
@Slf4j
public class BackupConsumer {

    @RabbitListener(queues = BACKUP_QUEUE_NAME)
    public void receiveD(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到 "+BACKUP_QUEUE_NAME+" {}", new Date(), msg);
    }


}
