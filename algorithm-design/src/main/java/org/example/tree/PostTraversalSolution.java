package org.example.tree;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 后序遍历迭代版
 */
public class PostTraversalSolution {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);

        new PostTraversalSolution().postorderTraversal(root);
    }

    public void postorderTraversal(TreeNode root) {
        if (root == null) {
            return;
        }
        Deque<TreeNode> stack = new LinkedList<>();
        pushLeftPathNodes(root, stack);
        while (!stack.isEmpty()) {
            doTraversal(stack);
        }
    }

    public void doTraversal(Deque<TreeNode> stack) {
        // 构建辅助栈
        Deque<TreeNode> tmpStack = new LinkedList<>();
        TreeNode node = stack.pop();
        tmpStack.push(node);
        if (node.right != null) {
            pushLeftPathNodes(node.right, tmpStack);
        }
        tmpStack.forEach(TreeNode::visit);
    }

    public void pushLeftPathNodes(TreeNode node, Deque<TreeNode> stack) {
        while (node != null) {
            stack.push(node);
            node = node.left;
        }
    }
}
