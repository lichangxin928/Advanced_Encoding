package com.lcx.webSocket;

import org.springframework.stereotype.Component;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * @author : lichangxin
 * @create : 2024/5/21 14:03
 * @description
 */

@ServerEndpoint("/chat/server")
@Component
public class ChatWebSocket {

    @OnOpen
    public void onOpen(Session session) {
    }


}
