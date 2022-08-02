package util;

import com.sun.istack.internal.NotNull;

public class SortUtil {

    private SortUtil() {}

    public static void print(int[] arr) {
        for (int x: arr) {
            System.out.print(x + "  ");
        }
        System.out.println();
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
