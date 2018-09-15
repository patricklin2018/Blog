/**
 * @Author: patrick-mac
 * @Date: 2018/9/14 09:40
 * @Description:
 */
public class InsertionSort {

    private InsertionSort() {}

    public static void sort(Comparable[] arr) {

        for (int i = 1; i < arr.length; ++i) {
            Comparable e = arr[i];
            int j = i - 1;
            for (; j >= 0 && e.compareTo(arr[j]) < 0; --j) {
                arr[j + 1] = arr[j];
            }
            arr[j + 1] = e;
        }
    }

    public static void main(String[] args) {
        int N = 10000;
        Integer[] arr = SortTestHelper.generateRandomArray(N, 0, N);
        SortTestHelper.testSort("InsertionSort", arr);
    }
}
