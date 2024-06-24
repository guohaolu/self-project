package org.example.pool;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 固定线程数的线程池
 */
@Data
public class FixedSizeThreadPoolDemoA {
    private final BlockingQueue<Runnable> taskQueue;

    private final List<Worker> workers;

    public FixedSizeThreadPoolDemoA(int poolSize, int queueSize) {
        taskQueue = new LinkedBlockingQueue<>(queueSize);
        workers = Stream.generate(() -> new Worker(this)).peek(Thread::start)
                .limit(poolSize).collect(Collectors.toList());
    }

    public static void main(String[] args) throws InterruptedException {
        FixedSizeThreadPoolDemoA pool = new FixedSizeThreadPoolDemoA(3, 6);
        for (int i = 0; i < 9; i++) {
            pool.submit(() -> {
                try {
                    TimeUnit.SECONDS.sleep(2L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName());
            });
        }
        TimeUnit.MINUTES.sleep(2L);
    }

    public boolean submit(Runnable task) {
        // 超过任务队列大小，提交的任务会被丢弃
        return taskQueue.offer(task);
    }

    @RequiredArgsConstructor
    private static class Worker extends Thread {
        private final FixedSizeThreadPoolDemoA threadPool;

        @Override
        public void run() {
            while (true) {
                try {
                    // 空则等待
                    Runnable task = threadPool.taskQueue.take();
                    task.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
