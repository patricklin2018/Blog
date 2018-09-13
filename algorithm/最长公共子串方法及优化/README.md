# 最长公共子串方法及优化

## 问题

[link 牛客网 - 问题链接](https://www.nowcoder.com/practice/02e7cc263f8a49e8b1e1dc9c116f7602?tpId=49&&tqId=29349&rp=1&ru=/activity/oj&qru=/ta/2016test/question-ranking)

对于两个字符串，请设计一个时间复杂度为 O(m*n) 的算法(这里的m和n为两串的长度)，求出两串的最长公共子串的长度。这里的最长公共子串的定义为两个序列`U1,U2,..Un`和`V1,V2,...Vn`，其中Ui + 1 == Ui+1,Vi + 1 == Vi+1，同时Ui == Vi。

给定两个字符串**A**和**B**，同时给定两串的长度**n**和**m**。

测试样例：

```
"1AB2345CD",9,"12345EF",7
返回：4
```

## 思路和实现

三种方法：

```
1. 动态规划（时间复杂度 = O(n^2)，空间复杂度 = O(n^2)）
2. 动态规划（时间复杂度 = O(n^2)，空间复杂度 = O(n)）
3. 动态规划（时间复杂度 = O(n^2)，空间复杂度 = O(1)）
```

### 1. 动态规划（时间复杂度 = O(n^2)，空间复杂度 = O(n^2)）

采用动态规划，相当于构建一张 n * m 大小的表，表中第 i 行，第 j 列代表以字符串 A[i] 和 B[j] 为结束的子串的最长公共子串长度。那么状态转移方程为：

```
if i == 0 || j == 0:
	dp[i][j] = A[i] == B[j] ? 1 : 0

else
	dp[i][j] = A[i] == B[j] ? dp[i - 1][j - 1] + 1 : 0
```

实现如下：

[方法1 代码链接 - Java](code/Solution1.java)

```
public class Solution1 {

    public int findLongest(String A, int n, String B, int m) {
        if (n == 0 || m == 0) {
            return 0;
        }

        int[][] dp = new int[n][m];
        int longest = 0;

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                if (A.charAt(i) == B.charAt(j)) {
                    if (i == 0 || j == 0) {
                        dp[i][j] = 1;
                    }
                    else {
                        dp[i][j] = dp[i - 1][j - 1] + 1;
                    }
                    if (dp[i][j] > longest) {
                        longest = dp[i][j];
                    }
                }
                else {
                    dp[i][j] = 0;
                }
            }
        }

        return longest;
    }
}
```

### 2. 动态规划（时间复杂度 = O(n^2)，空间复杂度 = O(n)）

从以上代码我们发现，执行过程为从第零行扫到最后一行，每一行从第零列扫到最后一列。而每一列的执行结果，要么等于 0 ，要么等于左上角加 1。因此，其实，用不着 n * m 的表格，只需要 2 * m 大小。通过与操作，确定当前行在 `dp[0][j]` 还是 `dp[1][j]`：

```
i from 0 to n - 1:
	cur = (i & 1)
	pre = ((i + 1) & 1)
	j from 0 to m - 1:
		// do dynamic planning
```

实现如下：

[方法2 代码链接 - Java](code/Solution2.java)

```java
public class Solution2 {

    public int findLongest(String A, int n, String B, int m) {
        if (n == 0 || m == 0) {
            return 0;
        }

        int[][] dp = new int[2][m];
        int longest = 0;

        for (int i = 0; i < n; ++i) {
            int cur = (i & 1);
            int pre = ((i + 1) & 1);
            for (int j = 0; j < m; ++j) {
                if (A.charAt(i) == B.charAt(j)) {
                    if (i == 0 || j == 0) {
                        dp[cur][j] = 1;
                    }
                    else {
                        dp[cur][j] = dp[pre][j - 1] + 1;
                    }
                    if (dp[cur][j] > longest) {
                        longest = dp[cur][j];
                    }
                }
                else {
                    dp[cur][j] = 0;
                }
            }
        }
        return longest;
    }
}
```

### 3. 动态规划（时间复杂度 = O(n^2)，空间复杂度 = O(1)）

上面两种方法都是一行一列地扫过去，我们知道，每一个结果只有两种，要么等于 0，要么等于左上角加 1， 因此，其实只要我们改变一下执行顺序，改变为从左上角执行到右上角的斜线执行方式，我们就只需要一个额外的变量空间。

实现如下：

[方法3 代码链接 - Java](code/Solution3.java)

```java
public class Solution3 {

    public int findLongest(String A, int n, String B, int m) {
        if (n == 0 || m == 0) {
            return 0;
        }

        int dp = 0;
        int longest = 0;

        // 上三角（包括对角线）
        for (int k = 0; k < m; ++k) {
            for (int i = 0, j = k; i < n && j < m; ++i, ++j) {
                if (A.charAt(i) == B.charAt(j)) {
                    if (i == 0 || j == 0) {
                        dp = 1;
                    }
                    else {
                        dp = dp + 1;
                    }
                    if (dp > longest) {
                        longest = dp;
                    }
                }
                else {
                    dp = 0;
                }
            }
        }
        // 下三角
        for (int k = 1; k < n; ++k) {
            for (int i = k, j = 0; i < n && j < m; ++i, ++j) {
                if (A.charAt(i) == B.charAt(j)) {
                    if (i == 0 || j == 0) {
                        dp = 1;
                    }
                    else {
                        dp = dp + 1;
                    }
                    if (dp > longest) {
                        longest = dp;
                    }
                }
                else {
                    dp = 0;
                }
            }
        }

        return longest;
    }
}

```

