package com.lcx.netty.handlerDemo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author : lichangxin
 * @create : 2024/7/16 14:01
 * @description
 */
public class ByteBufInboundHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("ByteBufInboundHandler 接收到消息");
        System.out.println(msg);
        ctx.fireChannelRead(msg);
    }
}
