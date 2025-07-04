package com.lcx.spring.controller;

import com.lcx.spring.callback.MQCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

import static com.lcx.spring.config.MQConfirmConfig.CONFIRM_EXCHANGE_NAME;

/**
 * @author : lichangxin
 * @create : 2024/5/28 9:59
 * @description
 */

@RestController
@RequestMapping("/confirm")
@Slf4j
public class ConfirmController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MQCallback mqCallback;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(mqCallback);
        /**
         * true：
         * 交换机无法将消息进行路由时，会将该消息返回给生产者
         * false：
         * 如果发现消息无法进行路由，则直接丢弃
         */
//        rabbitTemplate.setMandatory(true);
//        //设置回退消息交给谁处理
//        rabbitTemplate.setReturnCallback(mqCallback);
    }

    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message){
        //指定消息 id 为 1
        CorrelationData correlationData1=new CorrelationData("1");
        String routingKey="key1";

        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME,routingKey,message+routingKey,correlationData1);
        CorrelationData correlationData2=new CorrelationData("2");
        routingKey="key2";

        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME,routingKey,message+routingKey,correlationData2);
        log.info("发送消息内容:{}",message);
    }

}
