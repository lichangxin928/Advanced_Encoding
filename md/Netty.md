[netty](https://blog.csdn.net/qq_35190492/article/details/113174359)
## Netty 核心组件

### 1. Channel
​ Channel是 Java NIO 的一个基本构造。可以看作是传入或传出数据的载体。因此，它可以被打开或关闭，连接或者断开连接。

### 2. EventLoop 与 EventLoopGroup
​ EventLoop 定义了Netty的核心抽象，用来处理连接的生命周期中所发生的事件，在内部，将会为每个Channel分配一个EventLoop。

​ EventLoopGroup 是一个 EventLoop 池，包含很多的 EventLoop。

​ Netty 为每个 Channel 分配了一个 EventLoop，用于处理用户连接请求、对用户请求的处理等所有事件。EventLoop 本身只是一个线程驱动，在其生命周期内只会绑定一个线程，让该线程处理一个 Channel 的所有 IO 事件。

​ 一个 Channel 一旦与一个 EventLoop 相绑定，那么在 Channel 的整个生命周期内是不能改变的。一个 EventLoop 可以与多个 Channel 绑定。即 Channel 与 EventLoop 的关系是 n:1，而 EventLoop 与线程的关系是 1:1。

### 3. ServerBootstrap 与 Bootstrap
​ Bootstarp 和 ServerBootstrap 被称为引导类，指对应用程序进行配置，并使他运行起来的过程。Netty处理引导的方式是使的应用程序和网络层相隔离。

​ Bootstrap 是客户端的引导类，Bootstrap 在调用 bind()（连接UDP）和 connect()（连接TCP）方法时，会新创建一个 Channel，仅创建一个单独的、没有父 Channel 的 Channel 来实现所有的网络交换。

​ ServerBootstrap 是服务端的引导类，ServerBootstarp 在调用 bind() 方法时会创建一个 ServerChannel 来接受来自客户端的连接，并且该 ServerChannel 管理了多个子 Channel 用于同客户端之间的通信。

### 4. ChannelHandler 与 ChannelPipeline
​ ChannelHandler 是对 Channel 中数据的处理器，这些处理器可以是系统本身定义好的编解码器，也可以是用户自定义的。这些处理器会被统一添加到一个 ChannelPipeline 的对象中，然后按照添加的顺序对 Channel 中的数据进行依次处理。

### 5. ChannelFuture
​ Netty 中所有的 I/O 操作都是异步的，即操作不会立即得到返回结果，所以 Netty 中定义了一个 ChannelFuture 对象作为这个异步操作的“代言人”，表示异步操作本身。如果想获取到该异步操作的返回值，可以通过该异步操作对象的addListener() 方法为该异步操作添加监 NIO 网络编程框架 Netty 听器，为其注册回调：当结果出来后马上调用执行。

​ Netty 的异步编程模型都是建立在 Future 与回调概念之上的。

### 6. Netty 线程模型

Boss线程（也称为Accept线程）：负责接收客户端的连接请求，并将接收到的连接注册到Worker线程中，以便进行后续的I/O操作。在Netty中，可以通过创建一个NioEventLoopGroup作为Boss线程组来实现。
Worker线程（也称为I/O线程）：负责处理与客户端的连接，包括读取数据、写入数据等操作。Worker线程通常是一个线程池，可以处理多个客户端的连接请求。在Netty中，可以通过创建另一个NioEventLoopGroup作为Worker线程组来实现。

## Netty ChannelHandler 执行顺序
[ChannelHandler执行顺序](https://blog.csdn.net/bibiboyx/article/details/107322828)

**总结**

1、InboundHandler是通过fire事件决定是否要执行下一个InboundHandler，如果哪个InboundHandler没有调用fire事件，那么往后的Pipeline就断掉了。
2、InboundHandler是按照Pipleline的加载顺序，顺序执行。
3、OutboundHandler是按照Pipeline的加载顺序，逆序执行。
4、有效的InboundHandler是指通过fire事件能触达到的最后一个InboundHander。
5、如果想让所有的OutboundHandler都能被执行到，那么必须把OutboundHandler放在最后一个有效的InboundHandler之前。
6、推荐的做法是通过addFirst加载所有OutboundHandler，再通过addLast加载所有InboundHandler。
7、OutboundHandler是通过write方法实现Pipeline的串联的。
8、如果OutboundHandler在Pipeline的处理链上，其中一个OutboundHandler没有调用write方法，最终消息将不会发送出去。
9、ctx.writeAndFlush是从当前ChannelHandler开始，逆序向前执行OutboundHandler。
10、ctx.writeAndFlush所在ChannelHandler后面的OutboundHandler将不会被执行。
11、ctx.channel().writeAndFlush 是从最后一个OutboundHandler开始，依次逆序向前执行其他OutboundHandler，即使最后一个ChannelHandler是OutboundHandler，在InboundHandler之前，也会执行该OutbondHandler。
12、千万不要在OutboundHandler的write方法里执行ctx.channel().writeAndFlush，否则就死循环

## 使用 Netty 编写 Server

### 1. Netty Server
```java
package com.lcx.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

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
        int port = 9999; // 监听端口
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
                            pipeline.addLast(new NettyServerHandler());
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

```

### 2. Netty Server Handler

```java
package com.lcx.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author : lichangxin
 * @create : 2024/5/15 16:10
 * @description
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("客户端发送的消息是：" + msg);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.err.println("有新的客户端与服务器发生连接,客户端地址：" + channel.remoteAddress());
    }

    /**
     * 当有客户端与服务器断开连接时执行此方法，此时会自动将此客户端从 channelGroup 中移除
     * 1.打印提示信息
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.err.println("有客户端与服务器断开连接,客户端地址：" + channel.remoteAddress());
        channel.close();
        ctx.close();
    }

    /**
     * 表示channel 处于活动状态
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 处于活动状态");
        ctx.channel().writeAndFlush("test");
    }

    /**
     * 表示channel 处于不活动状态
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 处于不活动状态");
    }

}
```
### 3. Netty Client

```java
package com.lcx.server;

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

```

### 4. Netty Client Handler

```java
package com.lcx.server;

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

```


### Netty pipeline 中常用的方法使消息传递下去

在Netty的Pipeline中，消息传递通常是通过`ChannelHandlerContext`对象来完成的。Pipeline中的消息传递分为Inbound（入站）事件和Outbound（出站）事件，它们的传递方式和方法调用略有不同。

### Inbound事件（入站事件）

Inbound事件的传递方向是从下至上的，即从Pipeline的尾端（Tail）开始，经过各个Handler，最终到达Pipeline的头部（Head）。常用的Inbound事件传递方法包括：

1. `ChannelHandlerContext.fireChannelRegistered()`：当Channel被注册到它的EventLoop时调用。
2. `ChannelHandlerContext.fireChannelActive()`：当Channel变为活动状态时调用。
3. `ChannelHandlerContext.fireChannelRead(Object msg)`：当从Channel读取数据时被调用。
4. `ChannelHandlerContext.fireChannelReadComplete()`：当Channel上的读操作完成时被调用。
5. `ChannelHandlerContext.fireExceptionCaught(Throwable cause)`：当处理过程中发生异常时被调用。
6. `ChannelHandlerContext.fireUserEventTriggered(Object evt)`：当触发用户自定义事件时被调用。
7. `ChannelHandlerContext.fireChannelWritabilityChanged()`：当Channel的可写状态发生变化时被调用。
8. `ChannelHandlerContext.fireChannelInactive()`：当Channel离开活动状态并且不再连接其远程节点时调用。
9. `ChannelHandlerContext.fireChannelUnregistered()`：当Channel从它的EventLoop注销时调用。

这些方法通常都是由Netty的内部机制在适当的时候自动调用的，开发者在自定义的Handler中可以通过重写对应的方法来处理这些事件。

### Outbound事件（出站事件）

Outbound事件的传递方向是从上至下的，即从Pipeline的头部（Head）开始，经过各个Handler，最终到达Pipeline的尾端（Tail）。常用的Outbound事件传递方法包括：

1. `ChannelHandlerContext.bind(SocketAddress localAddress, ChannelPromise promise)`：绑定到给定的SocketAddress。
2. `ChannelHandlerContext.connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)`：连接到远程节点。
3. `ChannelHandlerContext.disconnect(ChannelPromise promise)`：断开与远程节点的连接。
4. `ChannelHandlerContext.close(ChannelPromise promise)`：关闭Channel。
5. `ChannelHandlerContext.write(Object msg, ChannelPromise promise)`：将给定的消息写入Channel。
6. `ChannelHandlerContext.writeAndFlush(Object msg, ChannelPromise promise)`：将给定的消息写入Channel，并立即刷新它。

这些方法通常是由开发者在自定义的Handler中显式调用的，用于触发出站操作。其中的`ChannelPromise`参数用于接收操作的结果，例如是否成功绑定、连接等。通过监听`ChannelPromise`的状态，开发者可以获取异步操作的结果并进行后续处理。


## API

### 1. Netty中ctx.writeAndFlush与ctx.channel().writeAndFlush的区别

在Netty中，`ctx.writeAndFlush()` 和 `ctx.channel().writeAndFlush()` 都用于向通道（Channel）写入数据并立即刷新（flush）数据，但它们之间有一个关键的上下文（Context）差异。

1. **ctx.writeAndFlush()**:

ctx.writeAndFlush(): 这个方法是在ChannelHandler中调用的，ctx表示ChannelHandlerContext，它包含了ChannelHandler和ChannelPipeline的信息。当你在ChannelHandler中调用ctx.writeAndFlush()时，消息会被写入到当前ChannelHandlerContext所关联的Channel，并且会沿着ChannelPipeline进行处理。

2. **ctx.channel().writeAndFlush()**:

这个方法直接通过Channel对象调用，而不是通过ChannelHandlerContext。它会将消息写入到Channel所关联的最后一个ChannelHandlerContext，并且会从最后一个ChannelHandlerContext开始沿着ChannelPipeline处理消息。这意味着，如果你在一个ChannelHandler之外的地方调用ctx.channel().writeAndFlush()，消息会从ChannelPipeline的最后一个ChannelHandler开始处理，而不是从当前ChannelHandler开始。


### 2. ChannelInboundHandler 生命周期

```java
package com.lcx.nettyHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author : lichangxin
 * @create : 2024/5/20 13:25
 * @description
 */
public class TestChannelInboundHandler extends SimpleChannelInboundHandler {
    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception { // 2
        System.out.println("channelRegistered");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        System.out.println("channelUnregistered");
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception { // 3
        System.out.println("channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        System.out.println("channelInactive");
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("channelRead");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("channelRead0");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
        System.out.println("channelReadComplete");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("userEventTriggered");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {
        System.out.println("channelWritabilityChanged");
    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception { // 1
        System.out.println("handlerAdded");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
        System.out.println("handlerRemoved");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
        System.out.println("exceptionCaught");
    }
}

```

1. handlerAdded
2. channelRegistered
3. channelActive
4. channelRead
5. channelReadComplete
6. channelInactive
7. channelUnregistered
8. handlerRemoved

### 3. ChannelOutBoundHandler 生命周期

```java
package com.lcx.nettyandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;

/**
 * @author : lichangxin
 * @create : 2024/5/20 13:26
 * @description
 */
public class TestChannelOutboundHandler implements ChannelOutboundHandler {
    @Override
    public void bind(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress, ChannelPromise channelPromise) throws Exception {
        System.out.println("bind");
    }

    @Override
    public void connect(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress, SocketAddress socketAddress1, ChannelPromise channelPromise) throws Exception {
        System.out.println("connect");
    }

    @Override
    public void disconnect(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        System.out.println("disconnect");
    }

    @Override
    public void close(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        System.out.println("close");
    }

    @Override
    public void deregister(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        System.out.println("deregister");
    }

    @Override
    public void read(ChannelHandlerContext channelHandlerContext) throws Exception { // 2
        System.out.println("read");
    }

    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object o, ChannelPromise channelPromise) throws Exception {
        System.out.println("write");
    }

    @Override
    public void flush(ChannelHandlerContext channelHandlerContext) throws Exception {
        System.out.println("flush");
    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {  // 1
        System.out.println("handlerAdded");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
        System.out.println("handlerRemoved");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
        System.out.println("exceptionCaught");
    }
}

```

1. handlerAdded
2. read
3. write
4. flush

### 4. 常用的关闭方法

在Netty中，处理网络I/O时，经常需要关闭连接或者等待连接关闭。`ctx.channel().closeFuture()`, `ctx.close()`, 和 `ctx.channel().close()` 这三个方法看似相似，但实际上它们的作用和使用的上下文有所不同。

#### 1. `ctx.channel().close()`

这个方法直接用于关闭`Channel`。`Channel`是Netty中网络I/O操作的基础，它代表了一个到实体（如一个硬件设备、一个文件、一个网络套接字或者能够执行一个或多个不同I/O操作的组件）的开放连接，如TCP/IP套接字连接。调用`close()`方法会尝试关闭这个`Channel`，并且释放与之关联的所有资源。如果在`ChannelHandler`的某个方法中调用此方法，它将导致Netty开始关闭这个连接的流程，但请注意，这个操作是异步的，即调用`close()`方法后，程序不会等待连接真正关闭后再继续执行。

#### 2. `ctx.close()`

这个方法通常是在`ChannelInboundHandler`或`ChannelOutboundHandler`的`channelRead()`, `exceptionCaught()`, 等方法内部被调用的。`ctx`是`ChannelHandlerContext`的实例，它代表了`ChannelHandler`和`ChannelPipeline`之间的关联。调用`ctx.close()`实际上等同于调用`ctx.channel().close()`，即它也会关闭`Channel`。但是，使用`ctx.close()`的一个好处是，它允许Netty的`ChannelPipeline`机制介入，这意味着如果在`ChannelPipeline`中有其他`ChannelHandler`对关闭事件感兴趣，它们将被通知到。

#### 3. `ctx.channel().closeFuture()`

这个方法返回的是一个`Future<Void>`对象，它代表了`Channel`关闭的完成状态。与`close()`方法不同，`closeFuture()`方法本身并不关闭`Channel`；它只是提供了一个机制，允许你以异步的方式等待`Channel`的关闭。当你对`closeFuture()`返回的`Future`对象调用`sync()`方法时，当前线程会阻塞，直到`Channel`被关闭。这在你需要等待连接关闭后再继续执行某些操作时非常有用。

#### 总结

- `ctx.channel().close()` 和 `ctx.close()` 都是用来关闭`Channel`的，但`ctx.close()`通过`ChannelHandlerContext`允许Netty的`ChannelPipeline`机制介入。
- `ctx.channel().closeFuture()` 返回一个`Future<Void>`对象，用于异步等待`Channel`关闭的完成。

选择使用哪个方法取决于你的具体需求，比如你是否需要等待连接关闭的完成，或者你是否想利用Netty的`ChannelPipeline`机制。

## Channel 常用配置

Netty Channel常用的一些配置参数包括但不限于以下几种：

1. **CONNECT_TIMEOUT_MILLIS**：这是Netty的连接超时参数，表示连接建立的超时时间，单位为毫秒。默认值为30000毫秒，即30秒。
2. **MAX_MESSAGES_PER_READ**：这个参数表示在一次Loop读取操作中能够读取的最大消息数。对于ServerChannel或者NioByteChannel，默认值为16；对于其他类型的Channel，默认值为1。
3. **WRITE_SPIN_COUNT**：这是Netty中用于控制一个Loop写操作执行的最大次数的参数。默认值为16。
4. **ALLOCATOR**：这是ByteBuf的分配器参数。在Netty 4.0版本中，默认值为UnpooledByteBufAllocator；在Netty 4.1版本中，默认值为PooledByteBufAllocator。
5. **RCVBUF_ALLOCATOR**：这个参数用于配置接收缓冲区的分配器。可选值包括FixedRecvByteBufAllocator（固定大小的接收缓冲区分配器）等。默认值为AdaptiveRecvByteBufAllocator.DEFAULT，这是一个可以根据实际接收到的数据大小动态调整接收缓冲区大小的分配器。
6. **WRITE_BUFFER_WATER_MARK**：这是写缓冲区的水位线参数。当写缓冲区中的数据量达到高水位线时，Netty会触发`ChannelWritabilityChanged`事件；当写缓冲区中的数据量低于低水位线时，Netty也会触发`ChannelWritabilityChanged`事件。

除了上述参数外，Netty Channel还有其他一些配置参数，这些参数可以根据具体的应用场景和需求进行配置。在Netty中，可以通过`ChannelConfig`接口来访问和修改这些参数。例如，可以通过`Channel.config()`方法来获取当前Channel的配置对象，然后调用配置对象的方法来修改相应的参数。

请注意，以上参数的具体含义和用法可能会随着Netty版本的更新而有所变化，因此建议查阅最新的Netty文档以获取最准确的信息。


## Netty 常用编码器和解码器

Netty 提供了许多常用的 decoder 和 encoder，这些编解码器可以帮助开发者在网络通信中方便地处理数据的编码和解码。以下是一些 Netty 中常用的 decoder 和 encoder：

**Decoder（解码器）**

1. **ByteToMessageDecoder**：这是一个基础的解码器，用于将字节数据（ByteBuf）解码为消息对象。开发者可以继承这个类并重写 `decode()` 方法来实现自定义的解码逻辑。
2. **StringDecoder**：将接收到的字节数据解码为字符串。它基于指定的字符集（默认为 UTF-8）进行解码。
3. **LineBasedFrameDecoder**：这是一个基于行的解码器，它按行切分数据帧。当从网络读取数据时，它会根据换行符（如 "\n" 或 "\r\n"）来切分数据，然后将每一行数据作为一个独立的消息对象进行解码。
4. **DelimiterBasedFrameDecoder**：这是一个基于特定分隔符的解码器。开发者可以指定一个或多个分隔符，当从网络读取数据时，它会根据这些分隔符来切分数据帧。
5. **RedisDecoder**：基于 Redis 协议的解码器，用于解析 Redis 服务器发送的命令和数据。
6. **HttpObjectDecoder**：基于 HTTP 协议的解码器，用于解析 HTTP 请求和响应消息。
7. **JsonObjectDecoder**：基于 JSON 数据格式的解码器，用于将 JSON 格式的字节数据解码为 Java 对象。

**Encoder（编码器）**

1. **MessageToByteEncoder**：这是一个基础的编码器，用于将消息对象编码为字节数据（ByteBuf）。开发者可以继承这个类并重写 `encode()` 方法来实现自定义的编码逻辑。
2. **StringEncoder**：将字符串编码为字节数据。它基于指定的字符集（默认为 UTF-8）进行编码。
3. **ObjectEncoder**：用于将 Java 对象序列化为字节数据。这通常与 `ObjectDecoder` 配合使用，以实现 Java 对象的网络传输。
4. **RedisEncoder**：基于 Redis 协议的编码器，用于将 Redis 命令和数据编码为字节数据，以便发送给 Redis 服务器。
5. **HttpObjectEncoder**：基于 HTTP 协议的编码器，用于将 HTTP 请求和响应消息编码为字节数据，以便发送给 HTTP 服务器或客户端。

这些编解码器可以根据需要组合使用，以实现复杂的网络通信协议。例如，在 HTTP 协议的客户端实现中，可能会使用 `HttpObjectDecoder` 和 `HttpObjectEncoder` 来处理 HTTP 请求和响应的编码和解码；而在 Redis 协议的客户端实现中，可能会使用 `RedisDecoder` 和 `RedisEncoder` 来处理 Redis 命令和数据的编码和解码。

## netty 分为 boss 线程和 worker线程的意义

在Netty服务器中，将线程分为Boss线程和Worker线程主要是为了提高网络通信的效率和并发处理能力。以下是关于这种线程划分的原因和优势的详细解释：

1. **职责明确**：
   - Boss线程：主要负责接收客户端的连接请求。一旦接收到连接请求，它会处理该请求，并将新建立的连接注册到Worker线程池中，以便后续的数据读写操作。
   - Worker线程：负责处理已经建立连接的数据读写操作。每个Worker线程都会处理一部分连接，从而实现并发处理多个连接的数据读写。

2. **线程数设置**：
   - Boss线程数通常设置为1，因为它主要负责接收连接请求，这是一个相对轻量级的操作，不需要太多的线程。
   - Worker线程数则可以根据服务器的CPU核心数量进行配置，通常是CPU核心数的两倍（即2*N，N为CPU核心数）。这样设置可以充分利用多核CPU的优势，提高并发处理能力。

3. **提高效率**：
   - Boss线程和Worker线程的分离使得Netty能够并行处理连接请求和数据读写操作，从而提高了网络通信的效率。
   - Boss线程专注于接收连接请求，而Worker线程则专注于处理数据读写，这种分工使得每个线程都能够专注于自己的任务，减少了线程间的切换开销。

4. **并发处理**：
   - Worker线程池中的多个线程可以并发处理多个连接的读写操作，从而实现了高并发处理。这种并发处理能力使得Netty能够轻松应对大量客户端的连接和数据传输。

5. **错误隔离**：
   - 由于Boss线程和Worker线程分别处理不同的任务，因此它们之间的错误可以相互隔离。如果某个Worker线程出现异常或错误，它不会影响其他Worker线程或Boss线程的正常运行。

6. **扩展性**：
   - Netty的这种线程模型具有很好的扩展性。通过调整Worker线程的数量，可以很容易地适应不同规模和负载的服务器环境。

