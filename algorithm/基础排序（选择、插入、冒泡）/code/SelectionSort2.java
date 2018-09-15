/**
 * @Author: patrick-mac
 * @Date: 2018/9/14 09:27
 * @Description:
 */
public class SelectionSort2 {

    private SelectionSort2() {}

    public static void sort(Comparable[] arr) {

        int left = 0, right = arr.length - 1;
        while (left < right) {

            int minIdx = left, maxIdx = right;

            // 保证 arr[minIdx] < arr[maxIdx]
            if (arr[minIdx].compareTo(arr[maxIdx]) > 0) {
                swap(arr, minIdx, maxIdx);
            }

            for (int i = left; i < right; ++i) {
                if (arr[i].compareTo(arr[minIdx]) < 0) {
                    minIdx = i;
                }
                else if (arr[i].compareTo(arr[maxIdx]) > 0) {
                    maxIdx = i;
                }
            }

            swap(arr, left, minIdx);
            swap(arr, right, maxIdx);
        }
    }

    public static void swap(Object[] arr, int i, int j) {
        Object tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void main(String[] args) {
        int N = 10000;
        Integer[] arr = SortTestHelper.generateRandomArray(N, 0, N);
        SortTestHelper.testSort("SelectionSort1", arr);
    }

}
