package org.example;

public class ExtractNumber {
    public static void main(String[] args) {
        int startYear = 2020;
        int startTerm = 2;
        int endYear = 2022;
        int endTerm = 1;

        for (int year = 2020; year <= 2022; year++) {
            for (int term = 1; term <= 2; term++) {
                if (year == 2020 && term < 2) {
                    continue;
                }
                if (year == 2022 && term > 1) {
                    continue;
                }
                System.out.println(year + "\t" + term + "\t");
            }
        }
    }


}