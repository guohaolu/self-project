package org.example;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.*;
import java.util.Arrays;

@Slf4j
public class EchoServer {
    public static void main(String[] args) {
        int port = 6666; // 服务器监听的端口号
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            log.info("服务器已启动，正在监听端口：" + port);

            // 服务器无限循环等待客户端的连接
            while (true) {
                Socket clientSocket = serverSocket.accept(); // accept阻塞，等待客户端connect
                log.info("客户端已连接：" + clientSocket.getInetAddress().getHostAddress());

                // 创建输入流和输出流
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // 读取客户端发送的消息
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    log.info("服务器接收到消息：" + inputLine);
                    // 将接收到的消息回送给客户端
                    out.println(inputLine);
                }

                // 关闭资源
                in.close();
                out.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            log.error("IO异常", e);
        }
    }

    @Override
    public String toString() {
        int[][] queries = new int[4][2];
        Arrays.stream(queries).map(arr -> arr[0] == arr[1]).toArray();
        return super.toString();
    }
}

