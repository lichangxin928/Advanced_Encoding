package com.lcx.netty.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author : lichangxin
 * @create : 2024/5/16 9:00
 * @description
 */
public class SocketServer {

    public static void main(String[] args) {
        int port = 12345; // 监听的端口

        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket socket = serverSocket.accept()) { // 等待客户端连接

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());

            // 从客户端读取数据
            int numberReceived = inputStream.readInt();
            int messageLength = inputStream.readInt();
            byte[] messageBytes = new byte[messageLength];
            inputStream.readFully(messageBytes);

            String message = new String(messageBytes);

            System.out.println("Number received: " + numberReceived);
            System.out.println("Message received: " + message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
