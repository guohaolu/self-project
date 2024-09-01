package org.example.socket.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * 客户端
 */
public class LaGouChannelClientDemo {
    public static void main(String[] args) throws IOException {
        try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 60001))) {
            socketChannel.write(ByteBuffer.wrap("发送一个请求1".getBytes(StandardCharsets.UTF_8)));
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int read = socketChannel.read(byteBuffer);
            System.out.println("服务端返回的消息：" + new String(byteBuffer.array(), 0, read, StandardCharsets.UTF_8));
        }
    }
}
