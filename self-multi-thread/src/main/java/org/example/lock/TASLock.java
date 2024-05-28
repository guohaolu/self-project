package org.example.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 使用TestAndSet同步原语来实现锁
 */
public class TASLock implements Lock {
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
