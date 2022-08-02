package sort;

public class MergeSortDemo {
    public static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, right, mid);
        }
    }

    private static void merge(int[] arr, int left, int right, int mid) {
        //两个有序子数组arr[left, mid], arr[mid+1, right]
        int i=left, j=mid+1, k=-1;
        int[] temp = new int[right-left+1];
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[++k] = arr[i++];
            } else {
                temp[++k] = arr[j++];
            }
        }
        if (i == mid+1) {
            while (j <= right) {
                temp[++k] = arr[j++];
            }
        } else {
            while (i <= mid) {
                temp[++k] = arr[i++];
            }
        }
        for (j -= 1; k >= 0;) {
            arr[j--] = temp[k--];
        }
    }

}
