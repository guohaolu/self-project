package org.example.lock;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TTAS
 */
public class TTASLock implements SimpleLock {
    /**
     * 锁标记，初始化为不上锁
     */
    private final AtomicBoolean lockFlag = new AtomicBoolean(false);

    @Override
    public void lock() {
        while (true) {
            // 读取缓存，避免直接使用TAS指令造成总线流量过大
            while (lockFlag.get());
            // 在未被上锁的时候才会去尝试TAS
            if (!lockFlag.getAndSet(true)) {
                return;
            }
        }
    }

    @Override
    public void unlock() {
        lockFlag.set(false);
    }
}
