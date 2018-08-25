## 通过前序、中序、后序相结合、水平遍历序列构建二叉树

前序、中序、后序共有三种结合方式，但是前序 + 后序构建的二叉树并不唯一。

| 结合方式    | 构建的二叉树 | LeetCode                                                     |
| ----------- | ------------ | ------------------------------------------------------------ |
| 前序 + 中序 | 唯一         | [link](https://leetcode-cn.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/description/) |
| 前序 + 后序 | 不唯一       | N/A                                                          |
| 中序 + 后序 | 唯一         | [link](https://leetcode-cn.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/description/) |
| 水平序列 | 唯一 | N/A |

**内容：**
1. 前序 + 中序 构建二叉树
2. 中序 + 后序 构建二叉树
3. 水平序列 构建二叉树

### 1. 前序 + 中序 构建二叉树

由于前序总是会先访问根节点再访问该根节点的左子树和右子树，而中序总是会先访问左子树，再访问根节点和右子树。

因此，结合两者，通过前序确认根节点，随后根据中序顺序，先于该根节点必为该节点的左子树，而后于该节点的必为该节点的右子树。如此循环，最终确定整科树。

以前序 `{1,2,4,7,3,5,6,8}` 和中序遍历序列 `{4,7,2,1,5,3,8,6}` 为例子。 

```java
1，通过前序第一个数值，知根为 1，查找 1 在中序的位置
2，中序 1 的左边 {4, 7, 2} 为左子树部分，与对应数量的前序序列 {2, 4, 7} 进入递归，构建该子树
3，中序 1 的右边 {5, 3, 8, 6} 为右子树部分，与对应数量的前序序列 {3, 5, 6, 8} 进入递归，构建该子树
```
代码如下：

```java
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
class Solution {
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        if (preorder == null || inorder == null || preorder.length == 0 || inorder.length == 0 || preorder.length != inorder.length) {
            return null;
        }

        // 1. 确定根节点
        TreeNode root = new TreeNode(preorder[0]);

        for (int i = 0; i < preorder.length; ++i) {
            if (preorder[0] == inorder[i]) {
                // 2. 中序根节点的左边为左子树，进入递归构建子树
                root.left = buildTree(Arrays.copyOfRange(preorder, 1, 1 + i), Arrays.copyOfRange(inorder, 0, i));
                // 3. 中序根节点的右边为右子树，进入递归构建子树
                root.right = buildTree(Arrays.copyOfRange(preorder, i + 1, preorder.length), Arrays.copyOfRange(inorder, i + 1, inorder.length));
                break;
            }
        }

        return root;
    }
}
```

### 2. 中序 + 后序 构建二叉树

与前序 + 中序思路相似。由于中序总是会先访问左子树，再访问根节点和右子树，而后序总是先访问左子树，再访问右子树，最后才访问根节点。
因此，结合两者，通过后序确认根节点，随后根据中序顺序，先于该根节点必为该节点的左子树，而后于该节点的必为该节点的右子树。如此循环，最终确定整科树。

以中序遍历序列 `[9,3,15,20,7]` 和后序遍历序列`[9,15,7,20,3]`为例子。

```java
1，通过后序最后一个数值 3，即根节点为 3，在中序查找 3 对应位置
2，中序 3 的左边 {9} 为左子树部分，与对应数量的后序序列 {9} 进入递归，构建该子树
3，中序 3 的右边 {15, 20, 7} 为右子树部分，与对应数量的前序序列 {15, 7, 20} 进入递归，构建该子树
```

代码如下：

```java
class Solution {
    public TreeNode buildTree(int[] inorder, int[] postorder) {
        if (inorder == null || postorder == null || inorder.length != postorder.length ||
                inorder.length == 0 || postorder.length == 0) {
            return null;
        }

        int sz = postorder.length;
        TreeNode root = new TreeNode(postorder[sz - 1]);
        for (int i = 0; i < sz && sz != 1; ++i) {
            if (inorder[i] == postorder[sz - 1]) {
                root.left = buildTree(Arrays.copyOfRange(inorder, 0, i), Arrays.copyOfRange(postorder, 0, i));
                root.right = buildTree(Arrays.copyOfRange(inorder, i + 1, sz), Arrays.copyOfRange(postorder, i, sz - 1));
                break;
            }
        }

        return root;
    }
}
```

### 3. 水平序列 构建二叉树

水平遍历序列构建二叉树需要用 `'#'` 一定标志符号标志 `null` 结点。

借用队列作为辅助，记录遍历顺序，当水平遍历序列遍历时，当前的位置即为队列头部的左右孩子。

代码如下：

```java
public class Solution {
    /**
     * @param level 利用 char 数组来表示水平遍历序列，其中，用 '#' 表示 null
     */
    public TreeNode buildTree(char[] level) {
        if (level == null || level.length == 0 || level[0] == '#') {
            return null;
        }

        TreeNode root = new TreeNode(level[0] - '0');
        Queue<TreeNode> list = new LinkedList<>();
        list.add(root);

        for (int i  = 1; i < level.length; ++i) {
            TreeNode parent = list.poll();
            if (level[i] != '#') {
                parent.left = new TreeNode(level[i] - '0');
                list.add(parent.left);
            }
            if (++i < level.length && level[i] != '#') {
                parent.right = new TreeNode(level[i] - '0');
                list.add(parent.right);
            }
        }

        return root;
    }
}
```