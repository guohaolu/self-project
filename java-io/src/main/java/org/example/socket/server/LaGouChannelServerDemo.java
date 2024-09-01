package org.example.socket.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 非阻塞服务端
 */
public class LaGouChannelServerDemo {
    public static void main(String[] args) throws IOException {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.socket().bind(new InetSocketAddress(60001));
            serverSocketChannel.configureBlocking(false);
            while (true) {
                // non-blocking
                try (SocketChannel socketChannel = serverSocketChannel.accept()) {
                    if (socketChannel != null) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        int read = socketChannel.read(byteBuffer);
                        System.out.println("客户端接收到的消息：" + new String(byteBuffer.array(), 0, read, StandardCharsets.UTF_8));
                        socketChannel.write(ByteBuffer.wrap("客户端已接收的您发送的请求".getBytes(StandardCharsets.UTF_8)));
                        socketChannel.close();
                    }
                    // do other work, sleep 1 second...
                    System.out.println("没有客户端连接。。。");
                    TimeUnit.SECONDS.sleep(1L);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
