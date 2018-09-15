/**
 * @Author: patrick-mac
 * @Date: 2018/9/14 09:12
 * @Description:
 */
public class SelectionSort1 {

    private SelectionSort1() {}

    public static void sort(Comparable[] arr) {
        for (int i = 0; i < arr.length; ++i) {
            int minIdx = i;
            for (int j = i; j < arr.length; ++j) {
                if (arr[j].compareTo(arr[minIdx]) < 0) {
                    minIdx = j;
                }
            }
            swap(arr, i, minIdx);
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
