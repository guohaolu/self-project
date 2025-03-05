package org.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * 已读状态
 */
@Service
public class ReadStatusService {
    private static final int BITS_PER_LONG = 64;

    // 设置用户已读状态
    public void markAsRead(long messageId, long userId) {
        int index = (int) (userId / BITS_PER_LONG);
        int bitPosition = (int) (userId % BITS_PER_LONG);

        // 从数据库中获取当前位图
        long[] bitmaps = getBitMapsFromDatabase(messageId);

        // 更新位图
        if (index < bitmaps.length) {
            bitmaps[index] |= (1L << bitPosition);
        } else {
            // 如果索引超出当前位图长度，扩展位图
            while (index >= bitmaps.length) {
                bitmaps = Arrays.copyOf(bitmaps, bitmaps.length + 1);
            }
            bitmaps[index] = 1L << bitPosition;
        }

        // 将更新后的位图保存到数据库
        saveBitMapsToDatabase(messageId, bitmaps);
    }

    // 获取已读用户数
    public int getReadCount(long messageId) {
        long[] bitmaps = getBitMapsFromDatabase(messageId);
        return countBitsSet(bitmaps);
    }

    // 获取未读用户数
    public int getUnreadCount(long messageId, int totalUsers) {
        return totalUsers - getReadCount(messageId);
    }

    // 计算位图中设置为1的位数
    private int countBitsSet(long[] bitmaps) {
        int count = 0;
        for (long bitmap : bitmaps) {
            while (bitmap != 0) {
                count += (int) (bitmap & 1);
                bitmap >>>= 1;
            }
        }
        return count;
    }

    // 从数据库中获取位图
    private long[] getBitMapsFromDatabase(long messageId) {
        // 实现从数据库读取位图的逻辑
        // 返回一个 long 数组
        return new long[] { messageId };
    }

    // 将位图保存到数据库
    private void saveBitMapsToDatabase(long messageId, long[] bitmaps) {
        // 实现将位图保存到数据库的逻辑
    }
}
