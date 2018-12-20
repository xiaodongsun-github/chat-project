package com.example.nio.client;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * <p></p>
 *
 * @author xiaodongsun
 * @date 2018/12/20
 */
public class NIOClient {

    public static void main(String[] args) throws Exception {
        //1.通道
        SocketChannel channel = SocketChannel.open();
        //2.设置非阻塞方式
        channel.configureBlocking(false);
        //3.服务器端ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 9999);
        //4.连接服务器端
        if (!channel.connect(inetSocketAddress)){
            //nio作为非阻塞的优势
            while (!channel.finishConnect()){
                System.out.println("Client： 连接服务器端同时可以做其他事情。。。。");
            }
        }
        //5.得到一个缓冲区，并存入数据
        String msg = "hello server";
        ByteBuffer writeBuf = ByteBuffer.wrap(msg.getBytes());
        //6.发送数据
        channel.write(writeBuf);

        System.in.read();
    }
}
