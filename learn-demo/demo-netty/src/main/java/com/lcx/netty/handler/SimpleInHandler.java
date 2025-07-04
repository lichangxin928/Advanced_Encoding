package com.lcx.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author : lichangxin
 * @create : 2024/6/4 13:18
 * @description
 */
public class SimpleInHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("simpleInbound receive message : " + msg);
    }
}
