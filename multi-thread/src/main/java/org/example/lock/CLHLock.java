package org.example.lock;

import lombok.Data;

import java.util.concurrent.atomic.AtomicReference;

/**
 * CLHLock
 */
public class CLHLock implements SimpleLock {
    /**
     * 线程私有变量，队列中的前驱节点, 当前节点的pre引用指向前驱节点的cur
     */
    private final ThreadLocal<Node> pre = ThreadLocal.withInitial(() -> null);

    /**
     * 线程私有变量，队列中的当前节点
     */
    private final ThreadLocal<Node> cur = ThreadLocal.withInitial(Node::new);

    /**
     * 全局变量，指向队列中的末尾节点
     */
    private final AtomicReference<Node> tail = new AtomicReference<>(new Node());

    @Override
    public void lock() {
        Node curTmpRef = cur.get();
        curTmpRef.setLocked(true);
        // 原子操作：插入队列
        Node preTmpRef = tail.getAndSet(curTmpRef);
        pre.set(preTmpRef);
        // 自旋前驱节点
        while (preTmpRef.isLocked());
    }

    @Override
    public void unlock() {
        // 下掉前驱节点
        pre.remove();
        // 解锁
        Node curTmpRef = cur.get();
        curTmpRef.setLocked(false);
    }

    /**
     * 队列节点
     */
    @Data
    private static class Node {
        /**
         * 是否上锁，默认为未上锁
         */
        private boolean locked;
    }
}
