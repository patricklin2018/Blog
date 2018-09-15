import java.util.Arrays;

/**
 * @Author: patrick-mac
 * @Date: 2018/9/14 14:32
 * @Description:
 */
public class MergeSort1 {

    private MergeSort1() {}

    public static void sort(Comparable[] arr) {
        sort(arr, 0, arr.length - 1);
    }

    public static void sort(Comparable[] arr, int left, int right) {

        if (left >= right) {
            return;
        }

        int mid = left + (right - left) / 2;

        sort(arr, left, mid);
        sort(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }

    public static void merge(Comparable[] arr, int left, int mid, int right) {
        Comparable[] backup = Arrays.copyOfRange(arr, left, right + 1);

        int i = left, j = mid + 1;
        for (int k = left; k <= right; ++k) {
            if (i > mid) {
                arr[k] = backup[j - left];
                j++;
            }
            else if (j > right) {
                arr[k] = backup[i - left];
                i++;
            }
            else if (backup[i - left].compareTo(backup[j - left]) <= 0) {
                arr[k] = backup[i - left];
                i++;
            }
            else {
                arr[k] = backup[j - left];
                j++;
            }
        }
    }

    public static void main(String[] args) {
        int N = 10000;
        Integer[] arr = SortTestHelper.generateRandomArray(N, 0, N);
        SortTestHelper.testSort("MergeSort1", arr);
    }

}
