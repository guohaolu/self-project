package org.example.socket.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 拉勾教育服务端
 */
public class LaGouServerDemo {
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(60001)) {
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.submit(() -> handler(socket));
            }
        }
    }

    private static void handler(Socket socket) {
        try {
            System.out.println("当前线程为：" + Thread.currentThread().getName());
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int read = inputStream.read(bytes);
            System.out.println("接受数据：" + new String(bytes, 0, read));
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("数据已被客户端接收".getBytes());
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
