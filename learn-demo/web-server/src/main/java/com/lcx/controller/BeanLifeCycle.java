package com.lcx.controller;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author : lichangxin
 * @create : 2024/8/27 9:45
 * @description
 */

public class BeanLifeCycle implements InitializingBean {

    static {
        System.out.println("static");
    }

    public BeanLifeCycle(){
        System.out.println("constructor");
    }

    public void init(){
        System.out.println("init-method");
    }

    public void destroy(){
        System.out.println("destroy-method");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");
    }

    @PostConstruct
    public void postConstruct(){
        System.out.println("postConstruct");
    }

    @Value("123")
    public void setValue(String value){
        System.out.println("setValue");
    }

    @PreDestroy
    public void preDestroy(){
        System.out.println("preDestroy");
    }

}
