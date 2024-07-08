package org.example.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * 哈希表抽象类
 */
public abstract class BaseHashSet<T> {
    /**
     * 哈希表
     */
    protected volatile List<T>[] table;

    /**
     * 长度
     */
    protected AtomicInteger size;

    @SuppressWarnings("unchecked")
    public BaseHashSet(int capacity) {
        size = new AtomicInteger(0);
        table = Stream.generate(ArrayList::new).limit(capacity).toArray(ArrayList[]::new);
    }

    /**
     * 是否含有此元素
     *
     * @param entry 元素
     * @return 是否含有
     */
    public boolean contains(T entry) {
        acquire(entry);
        try {
            int slot = entry.hashCode() % table.length;
            return table[slot].contains(entry);
        } finally {
            release(entry);
        }
    }

    /**
     * 新增元素
     *
     * @param entry 元素
     * @return 是否新增成功
     */
    public boolean add(T entry) {
        acquire(entry);
        try {
            int slot = entry.hashCode() % table.length;
            if (table[slot].contains(entry)) {
                return false;
            }
            table[slot].add(entry);
            size.incrementAndGet();
        } finally {
            release(entry);
        }
        // 考虑扩容
        if (policy(entry)) {
            resize(entry);
        }
        return true;
    }

    /**
     * 获得锁
     *
     * @param entry 元素
     */
    public abstract void acquire(T entry);

    /**
     * 释放锁
     *
     * @param entry 元素
     */
    public abstract void release(T entry);

    /**
     * 扩容
     *
     * @param entry 元素
     */
    public abstract void resize(T entry);

    /**
     * 检测是否达到扩容条件
     *
     * @param entry 元素
     * @return 是否达到扩容条件
     */
    public abstract boolean policy(T entry);
}
