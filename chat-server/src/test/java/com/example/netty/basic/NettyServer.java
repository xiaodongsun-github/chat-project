package com.example.netty.basic;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * <p></p>
 *
 * @author xiaodongsun
 * @date 2018/12/24
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        //1.创建一个线程组，接收客户端连接
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //2.创建一个线程组，处理网络操作
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        //3.创建服务器端启动助手来配置参数
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //4.设置两个线程组
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)//5.使用NioServerSocketChannel作为服务器端通道的实现
                .option(ChannelOption.SO_BACKLOG, 128)//6.设置线程队列中等待的连接数
                .childOption(ChannelOption.SO_KEEPALIVE, true)//7.保持活动连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() {//8.创建一个通道初始化对象
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {//9.往Pipeline链中添加自定义的handler类
                        sc.pipeline().addLast(new NettyServerHandler());
                    }
                });
        System.out.println("。。。。。。。。。。。Server is ready。。。。。。。。。。。");
        //10.绑定端口 非阻塞
        ChannelFuture cf = serverBootstrap.bind(9999).sync();
        System.out.println("=====================================Server is starting===================================");
        //11.关闭通道，关闭线程组
        cf.channel().closeFuture().sync();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
