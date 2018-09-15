# 高级排序（希尔、归并、快排、堆排）

**本文内容：**

```
一、希尔排序
二、归并排序
	1. 最初版本
	2. 优化
三、快速排序
	1. 最初版本
	2. 优化
	3. 二路快排
	4. 三路快排
四、堆排序
五、实验对比
```

| 排序算法 | 时间复杂度（一般情况下） | 最坏情况下 | 最好情况下 | 空间复杂度 | 稳定性 |
| -------- | ------------------------ | ---------- | ---------- | ---------- | ------ |
| 希尔排序 | O(nlogn)                 | O(n^2)     | O(n)       | O(1)       | 不稳定 |
| 归并排序 | O(nlogn)                 | O(nlogn)   | O(nlogn)   | O(n)       | 稳定   |
| 快速排序 | O(nlogn)                 | O(n^2)     | O(nlogn)   | O(nlogn)   | 不稳定 |
| 堆排序   | O(nlogn)                 | O(nlogn)   | O(nlogn)   | O(1)       | 不稳定 |

## 一、希尔排序

希尔排序概况：

```
时间复杂度：
	一般情况下 O(nlogn)
	最好情况下 O(n)
	最坏情况下 O(n^2)
空间复杂度：
	O(1)
稳定性：
	不稳定
```

思路就是按 gap 增量分区，即 `[1, 1 + gap, 1 + 2 * gap, ... , 1 + n * gap]` 为一个区，在区内进行插入排序。而 gap 从一开始 `gap = arr.length / 2 ` 不停递减，直到 `gap = 1`。

实现如下：

```java
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
```

## 二、归并排序

归并排序概况：

```
时间复杂度：
	一般情况下 O(nlogn)
	最好情况下 O(nlogn)
	最坏情况下 O(nlogn)
空间复杂度：
	O(n)
稳定性：
	稳定
```

### 1.  最初版本

分治，一直分到只剩下一个，再进行两两归并。

```java
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
```

### 2. 优化

优化：

* 规模较小时采用插入排序
* 验证，当左区间的最大值小于右区间最小值时省略 Merge 计算
* 辅助空间统一申请，不再是递归过程中各自申请

```java
public static void sort(Comparable[] arr) {
    Comparable[] assistant = new Comparable[arr.length];
    sort(arr, 0, arr.length - 1, assistant);
}

public static void sort(Comparable[] arr, int left, int right, Comparable[] assistant) {
    if (right - left <= 15) {
        // 规模较小插入排序
        InsertionSort(arr, left, right);
        return;
    }

    int mid = left + (right - left) / 2;

    sort(arr, left, mid, assistant);
    sort(arr, mid + 1, right, assistant);

    if (arr[mid].compareTo(arr[mid + 1]) >= 0) {
        merge(arr, left, mid, right, assistant);
    }
}

public static void merge(Comparable[] arr, int left, int mid, int right, Comparable[] assistant) {
    System.arraycopy(arr, left, assistant, left, right - left + 1);

    int i = left, j = mid + 1;
    for (int k = i; k <= right; ++k) {
        if (i > mid) {
            arr[k] = assistant[j++];
        }
        else if (j > right) {
            arr[k] = assistant[i++];
        }
        else if (assistant[i].compareTo(assistant[j]) <= 0) {
            arr[k] = assistant[i++];
        }
        else {
            arr[k] = assistant[j++];
        }
    }
}
```

## 三、 快速排序

快速排序概况：

```
时间复杂度：
	一般情况下 O(nlogn)
	最好情况下 O(nlogn)
	最坏情况下 O(n^2)
空间复杂度：
	O(nlogn)
稳定性：
	不稳定
```

### 1.  最初版本

选取第一个作为参照，将比其小的元素往其左边移动，比其大的往右边移动。

```
public static void sort(Comparable[] arr) {
    sort(arr, 0, arr.length - 1);
}

public static void sort(Comparable[] arr, int left, int right) {
	if (left >= right) {
		return;
	}

    int p = partition(arr, left, right);
    sort(arr, left, p - 1);
    sort(arr, p + 1, right);
}

public static int partition(Comparable[] arr, int left, int right) {
	Comparable e = arr[left];
	int p = left;
	for (int k = left + 1; k <= right; ++k) {
		if (arr[k].compareTo(e) < 0) {
			p++;
			swap(arr, k, p);
		}
	}
	swap(arr, left, p);
	return p;
}
```

### 2. 优化

总是采用第一个作为参照使得快排十分不稳定，为了尽可能让快排的结果左右两边的元素个数尽可能平衡，优化如下：

* 规模较小时，同样采用插入排序
* 采用随机参照，而不是第一个

```java
public static void sort(Comparable[] arr) {
    sort(arr, 0, arr.length - 1);
}

public static void sort(Comparable[] arr, int left, int right) {
    if (right - left <= 15) {
        // 插入排序
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

    int p = left;
    for (int k = left + 1; k <= right; ++k) {
        if (arr[k].compareTo(e) < 0) {
            p++;
            swap(arr, k, p);
        }
    }
    swap(arr, left, p);
    return p;
}
```

### 3. 二路快排

虽然随机标定的优化使得快排更加稳定，但是，当数组存在大量的相等时，以上的方法使得，右区间比左区间要长得多（等于参照的元素都排到了右区间）。针对这种情况，继续优化：

* 不再是将区间分成 `arr[左边] < 参照 <= arr[右边]` 的形式，而是改成 `arr[左边] <= 参照 <= arr[右边]`，将部分等于参照的放置在左区间，使得两边更趋平衡。

```java
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
```

### 4. 三路快排

优化：

* 相较于二路快排，三路快排加入了 `arr[i] == e` 的考虑，分治排序时，这部分就不用考虑。

```java
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
```

## 四、堆排序

堆排序概况：

```
时间复杂度：
	一般情况下 O(nlogn)
	最好情况下 O(nlogn)
	最坏情况下 O(nlogn)
空间复杂度：
	O(1)
稳定性：
	不稳定
```

```java
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
```

## 五、实验对比

```
经过 100 组大小为 1000000 的数据测试，结果如下：
ShellSort 平均时间 = 857 ms
MergeSort 平均时间 = 322 ms
MergeSort2 平均时间 = 261 ms
MergeSort3 平均时间 = 225 ms
QuickSort 平均时间 = 208 ms
QuickSort2 平均时间 = 197 ms
QuickSort2Ways 平均时间 = 196 ms
QuickSort3Ways 平均时间 = 330 ms
HeapSort 平均时间 = 497 ms
HeapSort2 平均时间 = 497 ms
HeapSort3 平均时间 = 477 ms
```



