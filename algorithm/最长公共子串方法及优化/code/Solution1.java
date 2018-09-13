/**
 * @Author: patrick-mac
 * @Date: 2018/9/13 09:47
 * @Description:
 */
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

    public static void main(String[] args) {
        new Solution1().findLongest("1AB2345CD",9,"12345EF",7);
    }
}
