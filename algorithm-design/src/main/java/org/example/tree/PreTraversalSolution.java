package org.example.tree;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 前序遍历，迭代版
 */
public class PreTraversalSolution {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);

        Deque<TreeNode> stack = new LinkedList<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            doTraversal(stack);
        }
    }

    /**
     * 一路向左访问，右孩子入栈
     *
     * @param stack 栈
     */
    private static void doTraversal(Deque<TreeNode> stack) {
        TreeNode node = stack.pop();
        while (node != null) {
            visit(node);
            if (node.right != null) {
                stack.push(node.right);
            }
            node = node.left;
        }
    }

    private static void visit(TreeNode node) {
        System.out.println(node.val);
    }
}
