/**
 * @Author: patrick-mac
 * @Date: 2018/9/14 10:53
 * @Description:
 */
public class ShellSort {

    private ShellSort() {}

    public static void sort(Comparable[] arr) {

        for (int gap = arr.length / 2; gap > 0; gap /= 2) {

            for (int i = gap; i < arr.length; ++i) {
                // 插入排序
                Comparable e = arr[i];
                int j = i - gap;
                for (; j >= 0 && e.compareTo(arr[j]) < 0; j -= gap) {
                    arr[j + gap] = arr[j];
                }
                arr[j + gap] = e;
            }

        }
    }

    public static void main(String[] args) {
        int N = 10000;
        Integer[] arr = SortTestHelper.generateRandomArray(N, 0, N);
        SortTestHelper.testSort("ShellSort", arr);
    }

}
