package org.example.lock;

/**
 * 提供上锁，解锁
 */
public interface SimpleLock {
    /**
     * 上锁
     */
    void lock();

    /**
     * 解锁
     */
    void unlock();
}
