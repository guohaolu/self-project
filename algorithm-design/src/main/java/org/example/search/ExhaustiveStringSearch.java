package org.example.search;

/**
 * 字符串搜索算法——暴力搜索
 */
public class ExhaustiveStringSearch {
    public static void main(String[] args) {
        ExhaustiveStringSearch search = new ExhaustiveStringSearch();
        String text = "abccccde";
        String pattern = "cde";
        System.out.println(search.isExist(pattern, text));
    }

    public boolean isExist(String pattern, String text) {
        for (int i = 0; i < text.length() - pattern.length() + 1; i++) {
            int j = 0;
            for (; j < pattern.length(); j++) {
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
            }
            if (j == pattern.length())
                return true;
        }
        return false;
    }
}
