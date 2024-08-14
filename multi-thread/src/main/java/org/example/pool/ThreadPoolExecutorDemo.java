package org.example.pool;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 */
public class ThreadPoolExecutorDemo {
    public static void main(String[] args) {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(20);
        System.out.println(executorService.getCorePoolSize());
        System.out.println(executorService.getPoolSize());
    }
}
