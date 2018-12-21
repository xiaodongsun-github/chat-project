package com.example.nio.server;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * <p></p>
 *
 * @author xiaodongsun
 * @date 2018/12/20
 */
public class NIOServer {

    public static void main(String[] args) throws Exception {
        //1.得到一个ServerSocketChannel对象
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //2.得到一个Selector对象
        Selector selector = Selector.open();

        //3.绑定一个端口
        serverSocketChannel.bind(new InetSocketAddress(9999));

        //4.设置非阻塞式
        serverSocketChannel.configureBlocking(false);

        //5.把ServerSocketChannel注册给Selector对象
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //6.服务器端操作
        while (true){
            //6.1监控客户端 nio非阻塞式优势
            if (selector.select(2000) == 0){
                System.out.println("Server： 没有客户端搭理， 可以做其他事");
                continue;
            }
            //6.2得到SelectionKey判断事件
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()){
                SelectionKey key = keyIterator.next();
                //客户端连接事件
                if (key.isAcceptable()){
                    System.out.println("OP_ACCEPT");
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_ACCEPT, ByteBuffer.allocate(1024));
                }
                //读取客户端事件
                if (key.isReadable()){
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("客户端发来数据： " + new String(buffer.array()));
                }
                //6.3手动集合中移除当前key，防止重复
                keyIterator.remove();
            }
        }
    }
}
