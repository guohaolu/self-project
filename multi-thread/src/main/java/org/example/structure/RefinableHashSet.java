package org.example.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

/**
 * 细粒度哈希集
 */
public class RefinableHashSet<T> extends BaseHashSet<T> {
    /**
     * 标志着哪个线程正在扩容，此时不应该操作集合
     */
    private final AtomicMarkableReference<Thread> owner;

    /**
     * 细粒度锁，可扩展
     */
    private volatile Lock[] locks;

    public RefinableHashSet(int capacity) {
        super(capacity);
        owner = new AtomicMarkableReference<>(null, false);
        locks = Stream.generate(ReentrantLock::new).limit(capacity).toArray(Lock[]::new);
    }

    /**
     * 获得锁，如果在扩容则自旋等待
     *
     * @param entry 元素
     */
    @Override
    public void acquire(T entry) {
        boolean[] markHolder = {true};
        Thread who;
        Thread me = Thread.currentThread();

        while (true) {
            // 判断是否有其他线程正在扩容
            do {
                who = owner.get(markHolder);
            } while (markHolder[0] && who != me);

            Lock[] oldLocks = locks;
            int slot = entry.hashCode() % table.length;
            Lock oldLock = oldLocks[slot];
            oldLock.lock();
            who = owner.get(markHolder);
            // 判断在加锁时，是否有其他线程正在扩容
            if ((!markHolder[0] || who == me) && oldLocks == locks) {
                return;
            }
            oldLock.unlock();
        }
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
        Thread me = Thread.currentThread();

        if (!owner.compareAndSet(null, me, false, true)) {
            return;
        }
        try {
            if (!policy()) {
                // 某个线程已经完成了扩容
                return;
            }
            quiesce();
            // 进行扩容
            int newCapacity = 2 * table.length;
            List<T>[] oldTable = table;
            table = Stream.generate(ArrayList::new).limit(newCapacity).toArray(ArrayList[]::new);
            locks = Stream.generate(ReentrantLock::new).limit(newCapacity).toArray(ReentrantLock[]::new);
            // 元素迁移至新表
            for (List<T> oldBucket : oldTable) {
                oldBucket.forEach(item -> {
                    int slot = item.hashCode() % table.length;
                    table[slot].add(item);
                });
            }
        } finally {
            owner.set(null, false);
        }
    }

    /**
     * 静止写操作
     */
    protected void quiesce() {
        for (Lock lock : locks) {
            while (((ReentrantLock) lock).isLocked());
        }
    }
}
