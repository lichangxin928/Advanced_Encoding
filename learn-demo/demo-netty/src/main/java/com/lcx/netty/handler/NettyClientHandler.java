package com.lcx.netty.handler;

import com.lcx.netty.server.NettyClient;
import com.lcx.netty.server.NettyServer;
import io.netty.channel.*;

/**
 * @author : lichangxin
 * @create : 2024/5/15 16:11
 * @description
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("客户端收到消息：" + msg);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.err.println("有新的客户端与服务器发生连接,客户端地址：" + channel.remoteAddress());
        NettyClient.channelGroup.add(channel);
    }

    /**
     * 当有客户端与服务器断开连接时执行此方法，此时会自动将此客户端从 channelGroup 中移除
     * 1.打印提示信息
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.err.println("有客户端与服务器断开连接,客户端地址：" + channel.remoteAddress());
    }

    /**
     * 表示channel 处于活动状态
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ctx.channel().writeAndFlush(Unpooled.copiedBuffer("Are you good?", CharsetUtil.UTF_8));
        System.out.println(ctx.channel().remoteAddress() + " 处于活动状态");
        NettyServer.channelSet.add(ctx.channel());
        System.out.println("添加 channel 完成");
    }

    /**
     * 表示channel 处于不活动状态
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 处于不活动状态");
    }
}
