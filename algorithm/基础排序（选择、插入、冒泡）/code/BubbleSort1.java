/**
 * @Author: patrick-mac
 * @Date: 2018/9/14 09:58
 * @Description:
 */
public class BubbleSort1 {

    private BubbleSort1() {}

    public static void sort(Comparable[] arr) {
        for (int i = 0; i < arr.length; ++i) {
            boolean isSwap = false;
            int right = arr.length - i;
            for (int j = 1; j < right; ++j) {
                if (arr[j - 1].compareTo(arr[j]) > 0) {
                    swap(arr, j, j - 1);
                    isSwap = true;
                }
            }
            if (isSwap == false) {
                break;
            }
        }
    }

    public static void swap(Object[] arr, int i, int j) {
        Object t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }

    public static void main(String[] args) {
        int N = 10000;
        Integer[] arr = SortTestHelper.generateRandomArray(N, 0, N);
        SortTestHelper.testSort("BubbleSort1", arr);
    }
}
