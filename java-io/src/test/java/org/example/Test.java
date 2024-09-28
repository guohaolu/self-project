package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 */
public class Test {
    public static void main(String[] args) {
        String str = "a;b;c";
        Arrays.stream(str.split(";")).map(Integer::parseInt).forEach(System.out::println);

    }
}
