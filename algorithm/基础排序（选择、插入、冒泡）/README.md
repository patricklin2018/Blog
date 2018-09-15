# 基础排序（选择、插入、冒泡）

**本文内容：**

```
一、选择排序
	1. 最初版本
	2. 优化
二、插入排序
三、冒泡排序
	1. 最初版本
	2. 优化
四、实验对比
```

综合对比：

| 排序算法 | 时间复杂度（一般情况下） | 最坏情况下 | 最好情况下 | 空间复杂度 | 稳定性 |
| -------- | ------------------------ | ---------- | ---------- | ---------- | ------ |
| 选择排序 | O(n^2)                   | O(n^2)     | O(n^2)     | O(1)       | 不稳定 |
| 插入排序 | O(n^2)                   | O(n^2)     | O(n)       | O(1)       | 稳定   |
| 冒泡排序 | O(n^2)                   | O(n^2)     | O(n)       | O(1)       | 稳定   |

## 一、选择排序  

选择排序概况：

```
时间复杂度：
	最好情况下 O(n^2)
	最坏情况下 O(n^2)
空间复杂度：
	O(1)
稳定性：
	不稳定
```

### 1. 最初版本

每次遍历都选出当前最小值，放入到相应的位置。

```java
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
```

### 2. 优化

优化：

* 每次遍历不仅选出最小值，还选出最大值。

```java
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
```

## 二、插入排序

插入排序概况：

```
时间复杂度：
	最好情况下 O(n)
	最坏情况下 O(n^2)
空间复杂度：
	O(1)
稳定性：
	稳定
```

思路就是类似平时扑克排整理，一张一张往已排序的序列中插入。

```java
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
```

## 三、冒泡排序

冒泡排序概况：

```
时间复杂度：
	最好情况下 O(n)
	最坏情况下 O(n^2)
空间复杂度：
	O(1)
稳定性：
	稳定
	（ps：当冒泡判定条件为 >= 的时候为不稳定）
```

### 1. 最初版本

从左往右冒泡，若没有冒泡交换，则终止。

```java
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
```

### 2. 优化

优化：

* 记录上一次循环中，最后交换的位置，本次循环就对该位置以后的序列不必进行遍历检验。

```java
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
```

## 四、实验结果对比

```
Test for random array, size = 20000, random range [0, 20000]
SelectionSort : 417 ms
SelectionSort2 : 373 ms
InsertionSort : 491 ms
BubbleSort : 1899 ms
BubbleSort2 : 1727 ms

Test for ordered random array, size = 20000, random range [0, 3]
SelectionSort : 336 ms
SelectionSort2 : 572 ms
InsertionSort : 285 ms
BubbleSort : 1290 ms
BubbleSort2 : 1177 ms

Test for nearly ordered array, size = 20000, swap time = 100
SelectionSort : 490 ms
SelectionSort2 : 295 ms
InsertionSort : 251 ms
BubbleSort : 1687 ms
BubbleSort2 : 1219 ms
```

