package sort;

import static util.SortUtil.swap;

public class SelectionSortDemo {

    public static void selectionSort(int[] arr, int left, int right) {
        int index;
        for (int i=left; i < right; i++) {
            index=i;
            for (int j=i; j <= right; j++) {
                if (arr[j] < arr[index]) {
                    index = j;
                }
            }
            swap(arr, i, index);
        }
    }
}
