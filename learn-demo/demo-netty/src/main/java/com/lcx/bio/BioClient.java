package com.lcx.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author : lichangxin
 * @create : 2024/5/30 15:23
 * @description
 */
public class BioClient {
    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = 7000;

        try (
                Socket socket = new Socket(host, port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            out.println("first Hello from client");
            String responseLine;
            while ((responseLine = in.readLine()) != null) {
                System.out.println("Received from server: " + responseLine);
                out.write("Hello from client");
            }
        }
    }
}
