package com.lcx.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : lichangxin
 * @create : 2024/5/27 14:07
 * @description
 */

@Slf4j
@RestController()
@RequestMapping("/send")
public class SendMessageController {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @GetMapping("/sendSimpleMsg")
    public void sendMsgSimple() {
        rabbitTemplate.convertAndSend("spring.simple.exchange","spring.simple.key","发送消息");
    }

    @GetMapping("/ttl/msg")
    public void sendTtlMessage(@RequestParam String msg,@RequestParam String delayTime) {
        rabbitTemplate.convertAndSend("spring.delay.exchange","spring.ttl.key",msg,message->{
            message.getMessageProperties().setExpiration(delayTime);
            return message;
        });
    }

    @GetMapping("/delay/msg")
    public void sendDelayMessage(@RequestParam String msg,@RequestParam Integer delayTime) {
        rabbitTemplate.convertAndSend("spring.delay.exchange","spring.delay.key",msg,message->{
            message.getMessageProperties().setDelay(delayTime);
            return message;
        });
    }

    @GetMapping
    public void sendMsg(@RequestParam String exchange,@RequestParam String key,@RequestParam String msg) {
        rabbitTemplate.convertAndSend(exchange,key,msg);
    }

}
