/**
 * @Author: patrick-mac
 * @Date: 2018/9/14 16:42
 * @Description:
 */
public class QuickSort3Ways {

    private QuickSort3Ways() {}

    public static void sort(Comparable[] arr) {
        sort(arr, 0, arr.length - 1);
    }

    public static void sort(Comparable[] arr, int left, int right) {
        if (right - left <= 15) {
            MergeSort2.InsertionSort(arr, left, right);
            return;
        }

        swap(arr, left, (int)Math.random() * (right - left + 1) + left);
        Comparable e = arr[left];

        // less 标定小于参照的最后一个元素
        int less = left;
        // greater 标定大于参照的第一个元素
        int greater = right + 1;

        int i = left + 1;
        while (i < greater) {
            if (arr[i].compareTo(e) == 0) {
                ++i;
            }
            else if (arr[i].compareTo(e) > 0) {
                swap(arr, i, --greater);
            }
            else {
                /**
                 * 这里与上面不同，这里可 i++，因为与前面对换，而换过来的值等于 e
                 * 而上面 swap(arr, i, greater--); 不可 i++，因为后面换过来的元素还没验证过
                 */
                swap(arr, i++, ++less);
            }
        }
        swap(arr, left, less);

        sort(arr, left, less - 1);
        sort(arr, greater, right);
    }

    public static void swap(Object[] arr, int i, int j) {
        Object tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void main(String[] args) {
        int N = 20000;
        Integer[] arr = SortTestHelper.generateRandomArray(N, 0, N);
//        Integer[] arr = {20, 8, 11, 17, 6, 0, 17, 16, 0, 6, 3, 12, 5, 15, 2, 20, 11, 14, 0, 12};
        SortTestHelper.testSort("QuickSort3Ways", arr);
    }
}
