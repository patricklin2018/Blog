/**
 * @Author: patrick-mac
 * @Date: 2018/9/14 10:17
 * @Description:
 */
public class BubbleSort2 {

    private BubbleSort2() {}

    public static void sort(Comparable[] arr) {

        int lastSwapIdxTmp = arr.length - 1;

        for (int i = 0; i < arr.length; ++i) {
            int lastSwapIdx = lastSwapIdxTmp;
            for (int j = 0; j < lastSwapIdx; ++j) {
                if (arr[j].compareTo(arr[j + 1]) > 0) {
                    swap(arr, j, j + 1);
                    lastSwapIdxTmp = j;
                }
            }
            if (lastSwapIdx == lastSwapIdxTmp) {
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
        SortTestHelper.testSort("BubbleSort2", arr);
    }

}
