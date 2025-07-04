package com.lcx.netty.handlerDemo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author : lichangxin
 * @create : 2024/7/16 14:24
 * @description
 */
public class IntegerInboundHandler extends SimpleChannelInboundHandler<Integer> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Integer msg) throws Exception {
        System.out.println("IntegerInboundHandler 接收到消息");
        System.out.println(msg);
        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("IntegerInboundHandler 出现异常");
        cause.printStackTrace();
//        ctx.fireExceptionCaught(cause);
    }
}
