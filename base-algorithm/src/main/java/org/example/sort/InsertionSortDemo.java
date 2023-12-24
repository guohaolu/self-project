package org.example.sort;

import org.example.util.SortUtil;

public class InsertionSortDemo {

    public static void insertionSort(int[] arr, int left, int right) {
        // left..i-1   i..right
        for (int i = left + 1; i <= right; i++) {
            for (int j = i; j > left && arr[j] < arr[j - 1]; j--) {
                SortUtil.swap(arr, j, j - 1);
            }
        }

    }
}
