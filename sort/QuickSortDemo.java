package sort;

import static util.SortUtil.swap;

public class QuickSortDemo {

    public static void quickSort(int[] arr,int left, int right) {
        //切分子数组直至只有1个元素
        if (left < right) {
            int pivot = partition(arr, left, right);
            quickSort(arr, pivot + 1, right);
            quickSort(arr, left, pivot - 1);
        }
    }

    public static int partition(int[] arr, int left, int right) {
        int i = left - 1, j = left;
        int p = arr[right];
        for (; j <= right; j++) {
            if (arr[j] <= p) {
                swap(arr, j, i + 1);
                i++;
            }
        }
        return i;

    }

}
