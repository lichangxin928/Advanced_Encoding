package com.lcx.netty.server;

import com.lcx.netty.handler.TestChannelInboundHandler;
import com.lcx.netty.handler.TestChannelOutboundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.HashSet;
import java.util.Set;

/**
 * @author : lichangxin
 * @create : 2024/5/15 16:08
 * @description
 */

public class NettyServer {

    public static Set<Channel> channelSet = new HashSet<>();

    public static void main(String[] args) throws Exception {
        int port = 10000; // 监听端口
        // 配置服务端NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new StringDecoder());
//                            pipeline.addLast(new NettyServerHandler());
                            pipeline.addLast(new TestChannelInboundHandler());
                            pipeline.addLast(new TestChannelOutboundHandler());
                        }
                    });

            // 开始监听并接受连接
            ChannelFuture f = b.bind(port).sync();

            // 等待服务器套接字关闭
            f.channel().closeFuture().sync();
        } finally {
            // 关闭所有EventLoopGroup以终止所有线程
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
