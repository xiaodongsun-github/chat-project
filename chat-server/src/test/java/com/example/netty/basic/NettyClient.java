package com.example.netty.basic;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * <p>网络客户端</p>
 *
 * @author xiaodongsun
 * @date 2018/12/24
 */
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {
        //1.客户端创建线程组
        NioEventLoopGroup group = new NioEventLoopGroup();
        //2.创建客户端的启动部署，完成相关配置
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettyClientHandler());
                    }
                });
        //7.客户端就绪
        System.out.println("========================== Client is ready ==========================");
        ChannelFuture future = bootstrap.connect("127.0.0.1", 9999).sync();
        //关闭连接
        future.channel().closeFuture().sync();
    }
}
