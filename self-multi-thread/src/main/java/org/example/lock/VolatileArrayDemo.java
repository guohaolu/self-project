package org.example.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 使用volatile关键字修饰数组
 */
@Slf4j
public class VolatileArrayDemo {
    static boolean x = false;

    static volatile boolean[] arr = new boolean[] {x, false};

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1L);
                log.info(String.valueOf(arr[0]));
                log.info(Arrays.toString(arr));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread1.start();
        x = true;
        // arr[0] = true;
        thread1.join();
    }
}
