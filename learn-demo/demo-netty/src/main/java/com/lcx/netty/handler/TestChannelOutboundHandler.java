package com.lcx.netty.handler;

import io.netty.channel.*;

import java.net.SocketAddress;

/**
 * @author : lichangxin
 * @create : 2024/5/20 13:26
 * @description
 */
public class TestChannelOutboundHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void bind(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress, ChannelPromise channelPromise) throws Exception {
        System.out.println("out bind");
    }

    @Override
    public void connect(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress, SocketAddress socketAddress1, ChannelPromise channelPromise) throws Exception {
        System.out.println("out connect");
    }

    @Override
    public void disconnect(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        System.out.println("out disconnect");
    }

    @Override
    public void close(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        System.out.println("out close");
    }

    @Override
    public void deregister(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        System.out.println("out deregister");
    }

    @Override
    public void read(ChannelHandlerContext channelHandlerContext) throws Exception { // 2
        System.out.println("out read");
        super.read(channelHandlerContext);
    }

    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object o, ChannelPromise channelPromise) throws Exception {
        System.out.println("out write");
        super.write(channelHandlerContext, o, channelPromise);
    }

    @Override
    public void flush(ChannelHandlerContext channelHandlerContext) throws Exception {
        System.out.println("out flush");
        super.flush(channelHandlerContext);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {  // 1
        System.out.println("out handlerAdded");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
        System.out.println("out handlerRemoved");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
        System.out.println("out exceptionCaught");
    }
}
