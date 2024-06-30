package org.example.lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * MCSLock，队列锁算法
 */
public class MCSLock implements SimpleLock {
    AtomicReference<Node> tail = null;

    ThreadLocal<Node> cur = ThreadLocal.withInitial(Node::new);

    @Override
    public void lock() {
        Node current = cur.get();
        Node pre = tail.getAndSet(current);
        if (pre == null) {
            return;
        }
        current.lockFlag = true;
        pre.next = current;
        // 自旋自己
        while (current.lockFlag);
    }

    @Override
    public void unlock() {
        Node current = cur.get();
        current.lockFlag = true;
        if (current.next == null) {

        }
    }

    private static class Node {
        volatile boolean lockFlag = false;
        volatile Node next;
    }
}
