package com.lcx.spring.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : lichangxin
 * @create : 2024/5/27 15:54
 * @description
 */

@Configuration
@Slf4j
public class MQDelayConfig {

    @Bean
    public CustomExchange CustomExchange() {
        Map<String, Object> args = new HashMap<>();
        //自定义交换机的类型
        args.put("x-delayed-type", "direct");
        return new CustomExchange("spring.delay.exchange","x-delayed-message",false,false,args);
    }

    @Bean
    public Queue DelayQueue() {
        //正常队列绑定死信队列信息
        Map<String, Object> params = new HashMap<>();
        //正常队列设置死信交换机 参数 key 是固定值
        params.put("x-dead-letter-exchange","spring.dead.exchange");
        //正常队列设置死信 routing-key 参数 key 是固定值
        params.put("x-dead-letter-routing-key", "spring.dead.key");
        return new Queue("spring.delay.queue1",false,false,false,params);
    }


    @Bean
    public Queue TTLQueue() {
        return new Queue("spring.ttl.queue",false,false,false,null);
    }

    @Bean
    Binding TTLBinding(@Qualifier("CustomExchange") CustomExchange customExchange,
                         @Qualifier("TTLQueue") Queue queue) {
        return  BindingBuilder.bind(queue).to(customExchange).with("spring.ttl.key").noargs();
    }

    @Bean
    Binding DelayBinding(@Qualifier("CustomExchange") CustomExchange customExchange,
                         @Qualifier("DelayQueue") Queue queue) {
        return  BindingBuilder.bind(queue).to(customExchange).with("spring.delay.key").noargs();
    }

    @Bean
    public DirectExchange DeadExchange() {
        return new DirectExchange("spring.dead.exchange");
    }

    @Bean
    public Queue DeadQueue() {
            return new Queue("spring.dead.queue");
    }

    @Bean
    Binding DeadBinding(@Qualifier("DeadExchange") DirectExchange directExchange, @Qualifier("DeadQueue") Queue queue) {
        return  BindingBuilder.bind(queue).to(directExchange).with("spring.dead.key");
    }



}
