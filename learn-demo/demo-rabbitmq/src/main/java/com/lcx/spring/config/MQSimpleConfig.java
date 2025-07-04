package com.lcx.spring.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : lichangxin
 * @create : 2024/5/27 14:14
 * @description
 */

@Configuration
public class MQSimpleConfig {


    @Bean
    public DirectExchange SimpleExchange() {
        return new DirectExchange("spring.simple.exchange");
    }

    @Bean
    public Queue SimpleQueue() {
        return new Queue("spring.simple.queue");
    }

    @Bean
    Binding SimpleBinding(@Qualifier("SimpleExchange") DirectExchange directExchange,@Qualifier("SimpleQueue") Queue queue) {
        return  BindingBuilder.bind(queue).to(directExchange).with("spring.simple.key");
    }


}
