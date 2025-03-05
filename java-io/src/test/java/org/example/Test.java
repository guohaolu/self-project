package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 */
public class Test {
    public static void main(String[] args) {
        List<MentorGrowthRecord> arr = Arrays.asList(new MentorGrowthRecord(null), new MentorGrowthRecord(null));
        List<MentorGrowthRecord> collect = arr.stream()
                .sorted(Comparator.comparing(MentorGrowthRecord::getDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Function.identity()));
    }

    @Data
    @AllArgsConstructor
    public static class MentorGrowthRecord {
        Date date;
    }
}
