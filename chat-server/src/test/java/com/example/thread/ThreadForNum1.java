package com.example.thread;

/**
 * <p>输出1</p>
 *
 * @author xiaodongsun
 * @date 2018/12/20
 */
public class ThreadForNum1 extends Thread {

    @Override
    public void run() {

        for (int i = 0; i < 10; i++) {
            synchronized (MyLock.o) {
                System.out.println("1");
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
