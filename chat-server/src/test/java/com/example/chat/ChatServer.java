package com.example.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * <p></p>
 *
 * @author xiaodongsun
 * @date 2018/12/21
 */
public class ChatServer {

    private Selector selector;
    private ServerSocketChannel listenerChannel;
    private static final int PORT = 9999;

    public ChatServer(){
        try {
            //得到选择器
            selector = Selector.open();
            //打开监听通道
            listenerChannel = ServerSocketChannel.open();
            //绑定端口
            listenerChannel.bind(new InetSocketAddress(PORT));
            //设置非阻塞模式
            listenerChannel.configureBlocking(false);
            //将选择器绑定到监听通道并accept事件
            listenerChannel.register(selector, SelectionKey.OP_ACCEPT);
            printInfo("Chat Server is Ready。。。。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start(){
        try {
            //不断轮循
            while (true){
                //获取就绪channel
                int count = selector.select();
                if(count > 0){
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        //监听到的accept
                        if (key.isAcceptable()){
                            SocketChannel sc = listenerChannel.accept();
                            //设置非阻塞模式
                            sc.configureBlocking(false);
                            //注册到选择器上并监听
                            sc.register(selector, SelectionKey.OP_READ);
                            System.out.println(sc.getRemoteAddress().toString().substring(1) + "上线了。。。。");
                            //将此对应的 channel 设置为 accept,接着准备接受其他客户端请求
                            key.interestOps(SelectionKey.OP_ACCEPT);
                        }

                        //监听到read
                        if (key.isReadable()){
                            //读取客户端发来的数据
                            readMsg(key);
                        }
                        iterator.remove();
                    }
                }else {
                    System.out.println("独自在寒风中等候。。。。");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMsg(SelectionKey key) {
        SocketChannel channel = null;
        try {
            //得到关联的通道
            channel = (SocketChannel) key.channel();
            //设置buffer缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //从通道中读取数据并存储到缓冲区
            int count = channel.read(buffer);
            //如果读取到有数据
            if (count > 0){
                //将缓冲区的数据转换为字符串
                String msg = new String(buffer.array());
                printInfo(msg);
                //把关联的channel设置为read，继续准备接受数据
                key.interestOps(SelectionKey.OP_READ);
                //向所有的客户端广播数据
                BroadCast(channel, msg);
            }
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                printInfo(channel.getRemoteAddress().toString().substring(1) + "下线了。。。");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void BroadCast(SocketChannel except, String msg) throws IOException {
        System.out.println("发送广播。。。");
        //广播数据到所有的SocketChannel中
        for (SelectionKey key:selector.keys()){
            Channel targetchannel = key.channel();
            //排除自身
            if (targetchannel instanceof SocketChannel && targetchannel != except){
                SocketChannel dest = (SocketChannel) targetchannel;
                //把数据存储到缓冲区中
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //往通道中写数据
                dest.write(buffer);
            }
        }
    }

    private void printInfo(String str){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("[" + sdf.format(new Date()) + "]" + str);
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.start();
    }

}
