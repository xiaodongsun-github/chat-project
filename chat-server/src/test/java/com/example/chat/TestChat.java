package com.example.chat;

import java.util.Scanner;

/**
 * <p></p>
 *
 * @author xiaodongsun
 * @date 2018/12/21
 */
public class TestChat {

    public static void main(String[] args) throws Exception {
        //创建一个聊天的客户端对象
        ChatClient chatClient = new ChatClient();

        new Thread(){
            @Override
            public void run() {
                chatClient.reciveMsg();

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        Scanner scanner = new Scanner(System.in);
        //在控制台输入数据并发送服务端
        while (scanner.hasNextLine()){
            String msg = scanner.nextLine();
            chatClient.sendMsg(msg);
        }
    }
}
