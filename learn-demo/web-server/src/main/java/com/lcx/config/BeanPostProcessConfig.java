package com.lcx.config;

import com.lcx.controller.BeanLifeCycle;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : lichangxin
 * @create : 2024/8/27 9:42
 * @description
 */

@Configuration
public class BeanPostProcessConfig implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof BeanLifeCycle){
            System.out.println("postProcessBeforeInitialization");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof BeanLifeCycle) {
            System.out.println("postProcessAfterInitialization");
        }
        return bean;
    }

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public BeanLifeCycle beanLifeCycle(){
        return new BeanLifeCycle();
    }


}
