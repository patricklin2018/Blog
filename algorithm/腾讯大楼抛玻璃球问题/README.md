# 腾讯大楼抛玻璃球问题

## 问题

腾讯大厦有39层，你手里有两颗一抹一眼的玻璃珠。当你拿着玻璃珠在某一层往下扔的时候，一定会有两个结果，玻璃珠碎了或者没碎。大厦有个临界楼层。低于它的楼层，往下扔玻璃珠，玻璃珠不会碎，等于或高于它的楼层，扔下玻璃珠，玻璃珠一定会碎。玻璃珠碎了就不能再扔。现在让你设计一种方式，使得在该方式下，最坏的情况扔的次数比其他任何方式最坏的次数都少。也就是设计一种最有效的方式。

## 分析

咱们先不论腾讯大厦具体为多少层。假设大楼为 N 层，那么 F(N) 为 N 层大厦最坏情况的最小值。

先假设一下，在第 10 层进行了抛球：

* 碎了，那么寻找临界楼层，最坏情况下只需要从 1 - 9 逐一遍历，也就是 10 次。
* 没碎，那么寻找临界楼层，相当于转换为 F(N - 10)  + 1 。其中 1 为本次。

也就是说，第 10 层的最坏情况为 max(10, F(N - 10) + 1)。 

那么 F(N) = min{ max(1, F(N - 1) + 1), max(2, F(N - 2) + 1), max(3, F(N - 3) + 1),  …… , max(N - 1, F(1) + 1)}

有了以上这个状态转移方程，就可以采用动态规划将其实现。

## 实现

```java
class Solution {
    public static void main(String[] args) throws IOException {
        int N = 0;
        Scanner scanner = new Scanner(System.in);
        while (N < 1) {
            N = scanner.nextInt();
        }

        int[] dp = new int[N + 1];
        dp[0] = 0;
        dp[1] = 1;

        for (int i = 2; i <= N; ++i) {
            int min = i;
            for (int j = 1; j < i; ++j) {
                int tmp = Math.max(j, dp[i - j] + 1);
                if (tmp < min) {
                    min = tmp;
                }
            }
            dp[i] = min;
        }

        System.out.println(dp[N]);
    }
}
```

实验结果为：

```
当 N = 39 时，最多只需要抛 9 次。
当 N = 100 时，最多只需要抛 14 次。
```



