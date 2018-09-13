/**
 * @Author: patrick-mac
 * @Date: 2018/9/13 10:18
 * @Description:
 */
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

    public static void main(String[] args) {
        new Solution3().findLongest("cacccca",7,"aaacca",6);
    }

}
