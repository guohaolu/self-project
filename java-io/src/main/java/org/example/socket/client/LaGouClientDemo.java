package org.example.socket.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 拉勾教育客户端
 */
public class LaGouClientDemo {
    public static void main(String[] args) throws IOException {
        while (true) {
            try (Socket socket = new Socket("127.0.0.1", 60001)) {
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                socket.getOutputStream().write(input.getBytes());
                socket.getOutputStream().flush();
                InputStream inputStream = socket.getInputStream();
                byte[] buffer = new byte[1024];
                int len = inputStream.read(buffer);
                System.out.println("服务端返回消息：" + new String(buffer, 0, len));
            }
        }
    }
}
