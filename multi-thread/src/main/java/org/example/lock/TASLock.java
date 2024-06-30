package org.example.lock;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 使用TestAndSet同步原语来实现锁
 */
public class TASLock implements SimpleLock {
     /**
     * 锁标记，初始化为不上锁
     */
    private final AtomicBoolean lockFlag = new AtomicBoolean(false);

    @Override
    public void lock() {
        // 如果从false -> true则返回，否则自旋
        while (lockFlag.getAndSet(true));
    }

    @Override
    public void unlock() {
        lockFlag.set(false);
    }
}
