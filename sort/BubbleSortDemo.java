package sort;

import static util.SortUtil.swap;

public class BubbleSortDemo {

    public static void bubbleSort(int[] arr, int left, int right) {
        boolean flag = true;
        int r = right;
        while (flag) {
            flag = false;
            for (int i = left; i < r; i++) {
                // 如果是 >= 就不能保持稳定
                if (arr[i] > arr[i+1]) {
                    swap(arr, i, i+1);
                    flag = true;
                }
            }
            r--;
        }

    }
}
