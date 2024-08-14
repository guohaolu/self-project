package org.example.thread;

import java.util.concurrent.TimeUnit;

/**
 * ThreadLocal测试
 */
public class ThreadLocalTest {
    private static final ThreadLocal<Integer> threadLocal = new InheritableThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        threadLocal.set(50);
        System.out.println(threadLocal.get());
        new Thread(() -> {
            System.out.println(threadLocal.get());
            threadLocal.set(30);
            System.out.println(threadLocal.get());
        }).start();
        TimeUnit.SECONDS.sleep(5);
        System.out.println(threadLocal.get());
    }
}
