package com.example.thread;

/**
 * <p></p>
 *
 * @author xiaodongsun
 * @date 2018/12/20
 */
public class TestThreadForNum {

    public static void main(String[] args) {
        new ThreadForNum1().start();
        new ThreadForNum2().start();
    }
}
