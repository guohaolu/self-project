package org.example.tree;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 给定一个二叉树，返回它的中序遍历。
 * <p>
 * 示例:
 * <p>
 * 输入: [1,null,2,3]
 * <p>
 * 输出: [1,3,2]
 * 进阶递归算法很简单，你可以通过迭代算法完成吗？
 */
public class InTraversalSolution {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);

        new InTraversalSolution().inorderTraversal(root);
    }

    public void inorderTraversal(TreeNode root) {
        if (root == null) {
            return;
        }
        Deque<TreeNode> stack = new LinkedList<>();
        // init
        pushLeftPathNodes(root, stack);
        while (!stack.isEmpty()) {
            doTraversal(stack);
        }
    }

    public void doTraversal(Deque<TreeNode> stack) {
        TreeNode node = stack.pop();
        // 访问节点，同时，右孩子左路径入栈
        node.visit();
        if (node.right != null) {
            pushLeftPathNodes(node.right, stack);
        }
    }

    public void pushLeftPathNodes(TreeNode node, Deque<TreeNode> stack) {
        while (node != null) {
            stack.push(node);
            node = node.left;
        }
    }
}
