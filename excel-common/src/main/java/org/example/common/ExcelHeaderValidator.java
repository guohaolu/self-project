package org.example.common;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.data.ReadCellData;

import java.util.*;
import java.util.stream.Collectors;

public class ExcelHeaderValidator {

    /**
     * 校验表头Map是否符合预期
     * @param headMap 实际表头Map（列号->列名）
     * @param expectedHeaders 预期表头名称集合
     * @throws RuntimeException 当表头不匹配时抛出
     */
    public static void validateHeaderMap(Map<Integer, ReadCellData<?>> headMap, Set<String> expectedHeaders) {
        // 将Map值转换为有序列表
        List<String> actualHeaders = headMap.values().stream()
                .map(ReadCellData::getStringValue)
                .map(String::trim)
                .toList();

        // 检查数量是否一致
        if (actualHeaders.size() != expectedHeaders.size()) {
            throw new RuntimeException(
                    String.format("表头数量不匹配，预期%d列，实际%d列",
                            expectedHeaders.size(), actualHeaders.size()));
        }

        // 检查表头内容是否一致
        Set<String> actualHeaderSet = new HashSet<>(actualHeaders);

        if (!actualHeaderSet.equals(expectedHeaders)) {
            Set<String> missingHeaders = expectedHeaders.stream()
                    .filter(h -> !actualHeaderSet.contains(h))
                    .collect(Collectors.toSet());

            Set<String> extraHeaders = actualHeaderSet.stream()
                    .filter(h -> !expectedHeaders.contains(h))
                    .collect(Collectors.toSet());

            throw new RuntimeException(
                    String.format("表头不匹配，缺少表头：%s，多余表头：%s",
                            missingHeaders, extraHeaders));
        }
    }

    public static Set<String> extractExpectedHeaders(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(ExcelProperty.class))
                .map(f -> {
                    ExcelProperty annotation = f.getAnnotation(ExcelProperty.class);
                    String[] values = annotation.value();
                    // 取第一个值作为表头名（支持多级表头）
                    return values.length > 0 ? values[0].trim() : f.getName();
                })
                .collect(Collectors.toSet());
    }
}