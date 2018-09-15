/**
 * @Author: patrick-mac
 * @Date: 2018/9/14 19:47
 * @Description:
 */
public class HeapSort {
    static class MinHeap<Item extends Comparable> {
        Item[] items;
        int capacity;
        int size;

        public MinHeap(int capacity) {
            this.items = (Item[]) new Comparable[capacity + 1];
            this.capacity = capacity + 1;
            this.size = 0;
        }

        // Heapify
        public MinHeap(Item[] arr) {
            this.items = (Item[]) new Comparable[arr.length + 1];
            this.capacity = arr.length + 1;

            for (int i = 0; i < arr.length; ++i) {
                items[i] = arr[i];
            }
            this.size = arr.length;

            for (int i = size / 2; i >= 1; --i) {
                shiftDown(i);
            }
        }

        public void insert(Item item) {
            assert size + 1 < capacity;

            items[++size] = item;
            shiftUp(size);
        }

        public Item extractMin() {
            assert size > 0;

            Item res = items[1];
            swap(1, size--);
            shiftDown(1);

            return res;
        }

        private void swap(int i, int j) {
            Item tmp = items[i];
            items[i] = items[j];
            items[j] = tmp;
        }

        private void shiftUp(int i) {
            assert i + 1 >= 1 && i + 1 <= capacity;

            Item e = items[i];
            while (i > 1 && items[i / 2].compareTo(e) > 0) {
                items[i] = items[i / 2];
                i /= 2;
            }
            items[i] = e;
        }

        private void shiftDown(int i) {
            assert i + 1 >= 1 && i + 1 <= capacity;

            Item e = items[i];
            while (i * 2 <= size) {
                int j = i * 2;
                if (j + 1 <= size && items[j + 1].compareTo(items[j]) < 0) {
                    j += 1;
                }

                if (items[j].compareTo(e) >= 0) {
                    break;
                }

                items[i] = items[j];
                i = j;
            }

            items[i] = e;
        }
    }

    public static void sort1(Comparable[] arr) {
        MinHeap<Comparable> heap = new MinHeap<>(arr.length);

        for (int i = 0; i < arr.length; ++i) {
            heap.insert(arr[i]);
        }

        for (int i = 0; i < arr.length; ++i) {
            arr[i] = heap.extractMin();
        }
    }

    public static void sort2(Comparable[] arr) {
        MinHeap<Comparable> heap = new MinHeap<>(arr);
    }

    public static void main(String[] args) {
        int N = 1000000;
        Integer[] arr = SortTestHelper.generateRandomArray(N, 0, N);
//        Integer[] arr = {1,4,1,9,9,7,10,7,3,4};
        SortTestHelper.testSort("HeapSort", arr);
    }
}
