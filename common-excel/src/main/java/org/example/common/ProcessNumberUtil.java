package org.example.common;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class ProcessNumberUtil {
    // 时间格式：年月日时分秒毫微纳
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
                    .withZone(ZoneId.systemDefault());

    // 随机后缀长度
    private static final int RANDOM_SUFFIX_LENGTH = 4;


    public static String generate() {
        // 获取当前时间的纳秒精度时间戳
        Instant now = Instant.now();

        // 格式化时间部分
        String timePart = FORMATTER.format(now);

        // 生成随机后缀防止冲突
        String randomPart = generateRandomSuffix();

        return "ACT" + timePart + randomPart;
    }

    private static String generateRandomSuffix() {
        // 生成4位随机数字
        int random = ThreadLocalRandom.current().nextInt(10000);
        return String.format("%04d", random);
    }
}
