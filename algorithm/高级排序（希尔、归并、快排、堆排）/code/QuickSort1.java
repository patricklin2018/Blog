/**
 * @Author: patrick-mac
 * @Date: 2018/9/14 15:34
 * @Description:
 */
public class QuickSort1 {

    private QuickSort1() {}

    public static void sort(Comparable[] arr) {
        sort(arr, 0, arr.length - 1);
    }

    public static void sort(Comparable[] arr, int left, int right) {
        if (left >= right) {
            return;
        }

        int p = partition(arr, left, right);
        sort(arr, left, p - 1);
        sort(arr, p + 1, right);
    }

    public static int partition(Comparable[] arr, int left, int right) {
        Comparable e = arr[left];
        int p = left;
        for (int k = left + 1; k <= right; ++k) {
            if (arr[k].compareTo(e) < 0) {
                p++;
                swap(arr, k, p);
            }
        }
        swap(arr, left, p);
        return p;
    }

    public static void swap(Object[] arr, int i, int j) {
        Object tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void main(String[] args) {
        int N = 10000;
        Integer[] arr = SortTestHelper.generateRandomArray(N, 0, N);
        SortTestHelper.testSort("QuickSort1", arr);
    }
}
