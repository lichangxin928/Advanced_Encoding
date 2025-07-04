package com.lcx.netty.handlerDemo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author : lichangxin
 * @create : 2024/7/16 14:23
 * @description
 */
public class StringInboundHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("StringInboundHandler 接收到消息");
        System.out.println(msg);
//        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("StringInboundHandler 出现异常");
        cause.printStackTrace();
//        ctx.fireExceptionCaught(cause);
    }
}
