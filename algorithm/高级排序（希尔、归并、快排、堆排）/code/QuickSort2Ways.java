/**
 * @Author: patrick-mac
 * @Date: 2018/9/14 16:22
 * @Description:
 */
public class QuickSort2Ways {

    private QuickSort2Ways() {}

    public static void sort(Comparable[] arr) {
        sort(arr, 0, arr.length - 1);
    }

    public static void sort(Comparable[] arr, int left, int right) {
        if (right - left <= 15) {
            MergeSort2.InsertionSort(arr, left, right);
            return;
        }

        int p = partition(arr, left, right);
        sort(arr, left, p - 1);
        sort(arr, p + 1, right);
    }

    public static int partition(Comparable[] arr, int left, int right) {
        swap(arr, left, (int)Math.random() * (right - left + 1) + left);
        Comparable e = arr[left];

        int i = left + 1, j = right;
        while (true) {
            /**
             * 这里采用 e.compareTo(arr[i]) > 0 和 e.compareTo(arr[j]) < 0 判定，
             * 而不采用 e.compareTo(arr[i]) >= 0 和 e.compareTo(arr[j]) <= 0 判定，
             * 当中间都是等于 参照e 时，虽然这么做，增加了 swap 的次数，但是，使得参照两边的区间更加平衡。
             */
            while (i <= right && e.compareTo(arr[i]) > 0) {
                ++i;
            }
            while (j >= left && e.compareTo(arr[j]) < 0) {
                --j;
            }
            if (i > j) {
                break;
            }

            swap(arr, i, j);
            i++;
            j--;
        }
        swap(arr, left, j);
        return j;
    }

    public static void swap(Object[] arr, int i, int j) {
        Object tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void main(String[] args) {
        int N = 10000;
        Integer[] arr = SortTestHelper.generateRandomArray(N, 0, N);
        SortTestHelper.testSort("QuickSort2Ways", arr);
    }
}
