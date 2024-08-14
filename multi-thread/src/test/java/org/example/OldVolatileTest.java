package org.example;

/**
 * 旧的volatile的关键字
 */
public class OldVolatileTest {
    int x = 0;
    volatile boolean v = false;

    public void writer() {
        x = 42;
        v = true;
    }

    public void reader() {
        if (v == true) {
            //uses x - guaranteed to see 42.
        }
    }
}
