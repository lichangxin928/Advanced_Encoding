package com.lcx.netty.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author : lichangxin
 * @create : 2024/5/16 8:59
 * @description
 */
public class SocketClient {

    public static void main(String[] args) {
        String host = "localhost"; // 服务器地址
        int port = 12345; // 服务器端口

        try (Socket socket = new Socket(host, port);
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {

            // 发送二进制数据
            // 例如，我们发送一个整数和一个字符串（字符串需要转换为字节数组）
            int numberToSend = 12345;
            String messageToSend = "Hello, Server!";
            byte[] messageBytes = messageToSend.getBytes();

            // 写入整数
            outputStream.writeInt(numberToSend);
            // 写入字符串的长度（可选，以便服务器知道要读取多少字节）
            outputStream.writeInt(messageBytes.length);
            // 写入字符串的字节
            outputStream.write(messageBytes);

            System.out.println("Data sent successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
