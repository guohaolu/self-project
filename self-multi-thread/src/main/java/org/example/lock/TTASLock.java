package org.example.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * TTAS
 */
public class TTASLock implements Lock {
    /**
     * 锁标记，初始化为不上锁
     */
    private final AtomicBoolean lockFlag = new AtomicBoolean(false);

    @Override
    public void lock() {
        while (true) {
            while (lockFlag.get());
            // 在未被上锁的时候才会去尝试TAS
            if (lockFlag.compareAndSet(false, true)) {
                return;
            }
        }
    }

    @Override
    public boolean tryLock() {
        return lockFlag.compareAndSet(false, true);
    }

    @Override
    public void unlock() {
        if (lockFlag.compareAndSet(true, false)) {
            throw new IllegalMonitorStateException("锁状态异常");
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
