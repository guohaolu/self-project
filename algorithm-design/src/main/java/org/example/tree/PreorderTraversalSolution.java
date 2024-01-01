package org.example.tree;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 144. 二叉树的前序遍历
 * 给你二叉树的根节点 root ，返回它节点值的 前序 遍历。
 * <p>
 * 示例 1：
 * <p>
 * 输入：root = [1,null,2,3]
 * 输出：[1,2,3]
 */
public class PreorderTraversalSolution {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        System.out.println(new PreorderTraversalSolution().preorderTraversal(root));
    }

    /**
     * 递归调用
     * <p>
     * stream方法中传入root树根，然后递归调用自身。
     * <p>
     * 存在的问题？
     * <ul>
     *     <li>如何收集，root的左子树返回List1，右子树返回List2，如何合并？本题中 => flatMap扁平化操作</li>
     *     <li>如何拆分，拆分方法写入map操作中，Function:node => node这棵子树的解</li>
     * </ul>
     */
    public List<Integer> preorderTraversal(TreeNode root) {
        if (root == null) {
            return new LinkedList<>();
        }
        return Stream.of(root).map(node -> {
            List<Integer> left = preorderTraversal(node.left);
            List<Integer> right = preorderTraversal(node.right);
            left.add(0, node.val); // root -> left -> right
            left.addAll(right);
            // left -> root -> right : left.add(node.val)
            // left -> right -> root : left.add(node.val)
            return left;
        }).flatMap(List::stream).collect(Collectors.toList());
    }
}
