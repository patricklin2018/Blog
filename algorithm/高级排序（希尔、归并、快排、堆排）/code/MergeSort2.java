import java.util.Arrays;

/**
 * @Author: patrick-mac
 * @Date: 2018/9/14 14:59
 * @Description:
 */
public class MergeSort2 {

    private MergeSort2() {}

    public static void sort(Comparable[] arr) {
        Comparable[] assistant = new Comparable[arr.length];
        sort(arr, 0, arr.length - 1, assistant);
    }

    public static void sort(Comparable[] arr, int left, int right, Comparable[] assistant) {
        if (right - left <= 15) {
            // 规模较小插入排序
            InsertionSort(arr, left, right);
            return;
        }

        int mid = left + (right - left) / 2;

        sort(arr, left, mid, assistant);
        sort(arr, mid + 1, right, assistant);

        if (arr[mid].compareTo(arr[mid + 1]) >= 0) {
            merge(arr, left, mid, right, assistant);
        }
    }

    public static void InsertionSort(Comparable[] arr, int left, int right) {
        for (int i = left + 1; i <= right; ++i) {
            int j = i - 1;
            Comparable e = arr[i];
            for (; j >= left && e.compareTo(arr[j]) < 0; --j) {
                arr[j + 1] = arr[j];
            }
            arr[j + 1] = e;
        }
    }

    public static void merge(Comparable[] arr, int left, int mid, int right, Comparable[] assistant) {
        System.arraycopy(arr, left, assistant, left, right - left + 1);

        int i = left, j = mid + 1;
        for (int k = i; k <= right; ++k) {
            if (i > mid) {
                arr[k] = assistant[j++];
            }
            else if (j > right) {
                arr[k] = assistant[i++];
            }
            else if (assistant[i].compareTo(assistant[j]) <= 0) {
                arr[k] = assistant[i++];
            }
            else {
                arr[k] = assistant[j++];
            }
        }
    }

    public static void main(String[] args) {
        int N = 10000;
        Integer[] arr = SortTestHelper.generateRandomArray(N, 0, N);
        SortTestHelper.testSort("MergeSort2", arr);
    }
}
