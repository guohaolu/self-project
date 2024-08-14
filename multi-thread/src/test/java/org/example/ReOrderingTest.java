package org.example;

/**
 * 重排序单元测试
 */
public class ReOrderingTest {
    int x = 0, y = 0;

    public void writer() {
        x = 1;
        y = 2;
    }

    public void reader() {
        int r1 = y;
        int r2 = x;
    }
}
