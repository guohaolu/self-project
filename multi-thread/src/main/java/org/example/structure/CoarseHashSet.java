package org.example.structure;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

/**
 * 粗粒度哈希
 */
@ToString
public class CoarseHashSet<T> extends BaseHashSet<T> {
    private final Lock lock;

    public CoarseHashSet(int capacity) {
        super(capacity);
        lock = new ReentrantLock();
    }

    /**
     * 获得锁
     *
     * @param entry 元素
     */
    @Override
    public void acquire(T entry) {
        lock.lock();
    }

    /**
     * 释放锁
     *
     * @param entry 元素
     */
    @Override
    public void release(T entry) {
        lock.unlock();
    }

    /**
     * 扩容
     *
     * @param entry 元素
     */
    @Override
    @SuppressWarnings("unchecked")
    public void resize(T entry) {
        lock.lock();
        try {
            if (!policy(entry)) {
                // 某个线程已经完成了扩容
                return;
            }
            // 进行扩容
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
            lock.unlock();
        }
    }

    /**
     * 检测是否达到扩容条件
     *
     * @param entry 元素
     * @return 是否达到扩容条件
     */
    @Override
    public boolean policy(T entry) {
        return size.get() / table.length > 4;
    }
}
