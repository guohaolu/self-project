package org.example;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.*;

@Slf4j
public class EchoClient {
    public static void main(String[] args) {
        String hostName = "localhost"; // 服务器的主机名或IP地址
        int port = 6666; // 服务器监听的端口号

        try {
            Socket echoSocket = new Socket(hostName, port); // connect客户端
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            String userInput;
            log.info("请输入消息（'bye'退出）：");
            while ((userInput = stdIn.readLine()) != null && !userInput.equalsIgnoreCase("bye")) {
                out.println(userInput); // 发送消息到服务器
                log.info("服务器回应：" + in.readLine()); // 读取并打印服务器的响应
            }

            // 关闭资源
            out.close();
            in.close();
            stdIn.close();
            echoSocket.close();
        } catch (UnknownHostException e) {
            log.error("尝试连接到未知的主机：" + hostName, e);
        } catch (IOException e) {
            log.error("无法获取I/O连接到：" + hostName, e);
        }
    }
}

