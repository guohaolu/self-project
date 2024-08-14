package org.example;

import lombok.extern.slf4j.Slf4j;

/**
 * final关键字修饰的数组，测试数组元素是否保证可见性.
 * 看到的是构造器结束时的最新值，并不是最新值
 */
@Slf4j
public class FinalArrayTest {
    private static final int[] array = new int[10]; // 数组声明为 final

    public static void main(String[] args) throws InterruptedException {
        Thread writerThread = new Thread(() -> {
            for (int i = 0; i < array.length; i++) {
                array[i] = i; // 修改数组中的元素
                log.info("Wrote: " + i);
                try {
                    Thread.sleep(100); // 为了让主线程有机会读取
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread readerThread = new Thread(() -> {
            while (true) {
                for (int i = 0; i < array.length; i++) {
                    log.info("Read: " + array[i]);
                }
                try {
                    Thread.sleep(100); // 为了观察变化
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        writerThread.start();
        readerThread.start();

        writerThread.join(); // 等待写入线程完成
        readerThread.interrupt(); // 中断读取线程
    }
}
