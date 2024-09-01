package org.example.socket.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * 使用多路选择器的服务端
 */
public class LaGouSelectorServerDemo {
    public static void main(String[] args) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.socket().bind(new InetSocketAddress(60001));
            serverSocketChannel.configureBlocking(false);

            try (Selector selector = Selector.open()) {
                // (1) register OP_ACCEPT event
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                while (true) {
                    if (selector.select(2 * 1000) <= 0) {
                        continue;
                    }
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        // (2) process OP_ACCEPT event
                        if (key.isAcceptable()) {
                            SocketChannel clientSocketChannel = serverSocketChannel.accept();
                            clientSocketChannel.configureBlocking(false);
                            // (3) register OP_READ event
                            clientSocketChannel.register(selector, SelectionKey.OP_READ);
                        }
                        // (4) process OP_READ event
                        else if (key.isReadable()) {
                            try (SocketChannel clientSocketChannel = (SocketChannel) key.channel()) {
                                ByteBuffer buffer = ByteBuffer.allocate(1024);
                                // (5) read client message
                                int read = clientSocketChannel.read(buffer);
                                if (read > 0) {
                                    System.out.println("接收到客户端消息：" + new String(buffer.array(), 0, read, StandardCharsets.UTF_8));
                                    // (6) request client message
                                    clientSocketChannel.write(ByteBuffer.wrap("客户端已接收到您的消息".getBytes(StandardCharsets.UTF_8)));
                                }
                            }
                        }
                        // (7) remove processed event
                        iterator.remove();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
