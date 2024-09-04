package org.example.structure;

import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

/**
 * Hash Set with lock striping, 锁分片技术
 */
@ToString
public class StripedHashSet<T> extends BaseHashSet<T> {
    /**
     * 分片锁，长度固定，不随哈希表扩容而增长
     */
    private final Lock[] locks;

    public StripedHashSet(int capacity) {
        super(capacity);
        locks = Stream.generate(ReentrantLock::new).limit(capacity).toArray(Lock[]::new);
    }

    /**
     * 获得锁
     *
     * @param entry 元素
     */
    @Override
    public void acquire(T entry) {
        int slot = entry.hashCode() % table.length;
        locks[slot].lock();
    }

    /**
     * 释放锁
     *
     * @param entry 元素
     */
    @Override
    public void release(T entry) {
        int slot = entry.hashCode() % table.length;
        locks[slot].unlock();
    }

    /**
     * 扩容
     */
    @Override
    @SuppressWarnings("unchecked")
    public void resize() {
        // 获取多个锁的操作会使线程在此阻塞，扩容时会停止其他所有操作
        Arrays.stream(locks).forEach(Lock::lock);
        try {
            if (!policy()) {
                // 某个线程已经完成了扩容
                return;
            }
            // 进行扩容，同CoarseHashSet
            int newCapacity = 2 * table.length;
            List<T>[] oldTable = table;
            table = Stream.generate(ArrayList::new).limit(newCapacity).toArray(ArrayList[]::new);
            for (List<T> oldBucket : oldTable) {
                oldBucket.forEach(item -> {
                    int slot = item.hashCode() % table.length;
                    table[slot].add(item);
                });
            }
        } finally {
            Arrays.stream(locks).forEach(Lock::unlock);
        }
    }
}
