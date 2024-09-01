package org.example.socket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.example.socket.netty.NettyClientHandler;

/**
 * Netty客户端
 */
public class NettyClientDemo {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap().group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelChain());

        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 60001).sync();
        // blocking
        channelFuture.channel().closeFuture().sync();
        group.shutdownGracefully();
    }

    private static class ChannelChain extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) {
            ch.pipeline().addLast(new NettyClientHandler());
        }
    }
}
