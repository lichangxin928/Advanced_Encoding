package com.lcx.netty.server;

import com.lcx.thread.ThreadPoolUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author : lichangxin
 * @create : 2024/5/6 13:17
 * @description
 */
public class InitSocketServer {

    public static int port = 18088;

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {

                Socket accept = serverSocket.accept();
                ThreadPoolExecutor executor = ThreadPoolUtils.getThreadPoolInstance("socketServerThreadPool");
                executor.execute(new ProxyServer(accept));

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
