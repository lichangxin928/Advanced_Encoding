package com.lcx.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author : lichangxin
 * @create : 2024/5/20 13:25
 * @description
 */
public class TestChannelInboundHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception { // 2
        System.out.println("in channelRegistered");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        System.out.println("in channelUnregistered");
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception { // 3
        System.out.println("in channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        System.out.println("in channelInactive");
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        ByteBuf byteBuf = Unpooled.buffer();
        System.out.println("in channelRead");
        channelHandlerContext.channel().writeAndFlush("12321");
        channelHandlerContext.fireChannelRead(o);
    }

//    @Override
//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String o) throws Exception {
//        ByteBuf byteBuf = Unpooled.buffer();
//        System.out.println("channelRead0");
//        channelHandlerContext.channel().writeAndFlush(byteBuf.writeInt(1));
//    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
        System.out.println("in channelReadComplete");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("in userEventTriggered");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {
        System.out.println("in channelWritabilityChanged");
    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception { // 1
        System.out.println("in handlerAdded");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
        System.out.println("in handlerRemoved");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
        System.out.println("in exceptionCaught");
    }
}
