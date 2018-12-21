package com.example.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * <p></p>
 *
 * @author xiaodongsun
 * @date 2018/12/21
 */
public class ChatClient {

    private static final String HOST = "127.0.0.1";
    private int PORT = 9999;
    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;

    public ChatClient() throws IOException {
        //得到选择器
        selector = Selector.open();
        //连接远程服务器
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //注册选择器并设置为read
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到客户端ip地址和端口信息，作为聊天用户名使用
        userName = socketChannel.getRemoteAddress().toString().substring(1);
        System.out.println("-------------------Client(" + userName + ")is ready ---------------------");
    }

    //向服务器端发送数据
    public void sendMsg(String msg) throws Exception{
        //如果控制台输入bye就关闭通道，结束聊天
        if (msg.equals("bye")){
            socketChannel.close();
            socketChannel = null;
            return;
        }
        msg = userName + "说： " + msg;
        try {
            //往通道中写数据
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //从服务器端接收消息
    public void reciveMsg(){
        try {
            int readyChannels = selector.select();
            //有可用的通道
            if (readyChannels > 0){
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()){
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()){
                        //得到关联的通道
                        SocketChannel sc= (SocketChannel) key.channel();
                        //得到一个缓冲区
                        ByteBuffer buff = ByteBuffer.allocate(1024);
                        //读取数据并存储到缓冲区
                        sc.read(buff);
                        //把缓冲区数据转换成字符串
                        String msg = new String(buff.array());
                        System.out.println(msg.trim());
                    }
                    //移除当前SelectorKey， 防止重复处理
                    keyIterator.remove();
                }
            }else {
                System.out.println("人呢?..........");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
