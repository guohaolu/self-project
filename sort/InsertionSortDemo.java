package sort;

import static util.SortUtil.swap;

public class InsertionSortDemo {

    public static void insertionSort(int[] arr, int left, int right) {
        // left..i-1   i..right
        for (int i = left + 1; i <= right; i++) {
            for (int j = i; j > left && arr[j] < arr[j-1]; j--) {
                swap(arr, j, j-1);
            }
        }
    }
}
