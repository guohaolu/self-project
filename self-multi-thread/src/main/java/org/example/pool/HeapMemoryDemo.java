package org.example.pool;

import java.util.concurrent.TimeUnit;

/**
 * 最大堆内存限制
 */
public class HeapMemoryDemo {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 2000; i++) {
            new Thread(() -> {
                try {
                    TimeUnit.MINUTES.sleep(10L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
        TimeUnit.MINUTES.sleep(10L);
    }
}

