package sortx;

import static sort.QuickSortDemo.partition;

public class SelectionKthMaxDemo {

    public static int SelectionKthMax(int[] arr, int left, int right, int k) {
        int pivot = partition(arr, left, right);
        if (k == pivot+1) {
            return arr[pivot];
        } else if (k < pivot+1) {
            return SelectionKthMax(arr, left, pivot-1, k);
        } else {
            return SelectionKthMax(arr, pivot+1, right, k);
        }
    }
}
