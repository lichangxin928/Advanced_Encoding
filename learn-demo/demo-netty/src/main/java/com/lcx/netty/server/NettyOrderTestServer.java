package com.lcx.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author : lichangxin
 * @create : 2024/6/4 13:53
 * @description 验证 netty 中 handler 中的顺序，分为 inbound handler 和 outbound handler
 *
 * 1、InboundHandler是通过fire事件决定是否要执行下一个InboundHandler，如果哪个InboundHandler没有调用fire事件，那么往后的Pipeline就断掉了。
 * 2、InboundHandler是按照Pipleline的加载顺序，顺序执行。
 * 3、OutboundHandler是按照Pipeline的加载顺序，逆序执行。
 * 4、有效的InboundHandler是指通过fire事件能触达到的最后一个InboundHander。
 * 5、如果想让所有的OutboundHandler都能被执行到，那么必须把OutboundHandler放在最后一个有效的InboundHandler之前。
 * 6、推荐的做法是通过addFirst加载所有OutboundHandler，再通过addLast加载所有InboundHandler。
 * 7、OutboundHandler是通过write方法实现Pipeline的串联的。
 * 8、如果OutboundHandler在Pipeline的处理链上，其中一个OutboundHandler没有调用write方法，最终消息将不会发送出去。
 * 9、ctx.writeAndFlush是从当前ChannelHandler开始，逆序向前执行OutboundHandler。
 * 10、ctx.writeAndFlush所在ChannelHandler后面的OutboundHandler将不会被执行。
 * 11、ctx.channel().writeAndFlush 是从最后一个OutboundHandler开始，依次逆序向前执行其他OutboundHandler，即使最后一个ChannelHandler是OutboundHandler，在InboundHandler之前，也会执行该OutbondHandler。
 * 12、千万不要在OutboundHandler的write方法里执行ctx.channel().writeAndFlush，否则就死循环
 */
public class NettyOrderTestServer {

    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();

        ChannelFuture future = new ServerBootstrap()
                .group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                System.out.println(1);
                                ctx.fireChannelRead(msg); // 1
                            }
                        });

                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                System.out.println(2);
                                ctx.fireChannelRead(msg); // 2
                            }
                        });
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                System.out.println(3);
                                ctx.channel().writeAndFlush(msg); // 3
//                                ctx.fireChannelRead(msg);
                            }
                        });
                        ch.pipeline().addFirst(new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg,
                                              ChannelPromise promise) {
                                System.out.println(4);
                                ctx.write(msg, promise); // 4
                            }
                        });
                        ch.pipeline().addFirst(new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg,
                                              ChannelPromise promise) {
                                System.out.println(5);
                                ctx.write(msg, promise); // 5
                            }
                        });
                        ch.pipeline().addFirst(new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg,
                                              ChannelPromise promise) {
                                System.out.println(6);
                                ctx.write(msg, promise); // 6
                            }
                        });
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                System.out.println("server receive message: " + msg);
//                                ctx.writeAndFlush(msg);
//                                ctx.fireChannelRead(msg);
                            }

                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                System.out.println("active");
//                                super.channelActive(ctx);
                            }
                        });

                    }
                }).bind(18889);
        future.sync().channel().closeFuture();

    }

}
