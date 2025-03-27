package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 */
public class Test {
    public static void main(String[] args) {
        String date = LocalDate.now().atStartOfDay().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
