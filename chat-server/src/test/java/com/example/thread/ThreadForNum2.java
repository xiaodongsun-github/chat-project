package com.example.thread;

/**
 * <p>输出2</p>
 *
 * @author xiaodongsun
 * @date 2018/12/20
 */
public class ThreadForNum2 extends Thread {

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            synchronized (MyLock.o) {
                System.out.println("2");
                MyLock.o.notify();
                try {
                    MyLock.o.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
