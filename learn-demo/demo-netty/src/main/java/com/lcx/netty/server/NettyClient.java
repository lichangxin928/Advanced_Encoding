package com.lcx.netty.server;

import com.lcx.netty.handler.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : lichangxin
 * @create : 2024/5/15 16:11
 * @description
 */
@Slf4j
public class NettyClient {

    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static String host = "127.0.0.1";

    private static int port = 9999;

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new NettyClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();


            log.info("TCPClient Start , Connect host:"+host+":"+port);
//                future.channel().closeFuture().sync();
            future.channel().writeAndFlush(createMessage());

        } catch (Exception e) {
            try {
                log.error("TCPClient Error", e);
                log.error("TCPClient Retry Connect host:\"+host+\":\"+port");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    private static ByteBuf createMessage() {
        // 创建一个ByteBuf，并写入数据
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(12345); // 写入一个整数
        buffer.writeBytes("Hello, Server!".getBytes()); // 写入一个字符串
        return buffer;
    }
}
