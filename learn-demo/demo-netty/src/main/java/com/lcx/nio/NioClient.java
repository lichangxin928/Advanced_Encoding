package com.lcx.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClient {

    public static void main(String[] args)  {


        try {
            // 创建一个SocketChannel并设置为非阻塞模式
            SocketChannel socketChannel = SocketChannel.open(); ;
            socketChannel.configureBlocking(false);

            // 连接到服务器
            socketChannel.connect(new InetSocketAddress("localhost", 7001));

            // 等待连接完成（这里只是简单示例，实际应用中可能需要更复杂的逻辑）
            while (!socketChannel.finishConnect()) {
                // 非阻塞模式，可以做其他事情...
                // Thread.sleep(1000); // 例如等待一段时间再尝试连接
            }

            // 发送数据
            String message = "Hello from NIO Client!";
            ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }

            // 关闭通道
            socketChannel.close();
            socketChannel = SocketChannel.open();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}