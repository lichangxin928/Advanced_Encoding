package com.lcx.netty.handlerDemo;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;

/**
 * @ClassName EchoOutboundHandler1
 * @Description TODO
 * @Author felix
 * @Date 2019/9/27 15:36
 * @Version 1.0
 **/
public class EchoOutboundHandler1 extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("进入 EchoOutboundHandler1.write");

        //ctx.writeAndFlush(Unpooled.copiedBuffer("[第一次write中的write]", CharsetUtil.UTF_8));
//        ctx.channel().writeAndFlush(Unpooled.copiedBuffer("在OutboundHandler里测试一下channel().writeAndFlush", CharsetUtil.UTF_8));
        ctx.writeAndFlush(msg);

        System.out.println("退出 EchoOutboundHandler1.write");
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        System.out.println("EchoOutboundHandler1 read");
        super.read(ctx);
    }
}