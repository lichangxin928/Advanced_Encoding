package com.lcx.netty.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * @author : lichangxin
 * @create : 2024/5/6 10:58
 * @description
 */
public class ProxyServer implements Runnable{

    Socket socket;
    public ProxyServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        byte[] bytes = new byte[1024];

        try {
            InputStream inputStream = socket.getInputStream();
            while (-1 != inputStream.read(bytes)) {
                System.out.println(new String(bytes));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
