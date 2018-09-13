/**
 * @Author: patrick-mac
 * @Date: 2018/9/13 09:57
 * @Description:
 */
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

    public static void main(String[] args) {
        new Solution2().findLongest("cacccca",7,"aaacca",6);
    }
}
