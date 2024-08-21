package org.example.twopointer;

import java.util.*;

/**
 * 双指针
 */
public class Solution {
    public int[] twoSum(int[] numbers, int target) {
        int i = 0, j = numbers.length - 1;
        while (i < j) {
            int sum = numbers[i] + numbers[j];
            if (sum == target) {
                return new int[]{i + 1, j + 1};
            } else if (sum < target) {
                i++;
            } else {
                j--;
            }
        }
        return new int[]{};
    }

    public void moveZeroes(int[] nums) {
        int j = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                nums[j] = nums[i];
                j++;
            }
        }
        for (; j < nums.length; j++) {
            nums[j] = 0;
        }
    }

    public int maxArea(int[] height) {
        int left = 0, right = height.length - 1;
        int max = (right - left) * Math.min(height[left], height[right]);
        while (left < right) {
            if (height[left] <= height[right]) {
                left++;
            } else {
                right--;
            }
            max = Math.max(max, (right - left) * Math.min(height[left], height[right]));
        }
        return max;
    }

    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0) {
                break;
            }
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int left = i + 1, right = nums.length - 1;
            while (left < right) {
                if (left - i > 1 && nums[left] == nums[left - 1]) {
                    left++;
                    continue;
                }
                if (right < nums.length - 1 && nums[right] == nums[right + 1]) {
                    right--;
                    continue;
                }
                int sum = nums[left] + nums[right];
                if (sum == -nums[i]) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    left++;
                    right--;
                } else if (sum < -nums[i]) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.trap(new int[]{0,1,0,2,1,0,1,3,2,1,2,1}));
    }

    public int trap(int[] height) {
        int sum = 0;
        // idx , value
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparing((int[] x) -> x[1]).reversed());
        for (int i = 0; i < height.length - 1;) {
            while (i < height.length - 1 && height[i + 1] >= height[i]) {
                i++;
            }
            queue.add(new int[]{i, height[i]});
            while (i < height.length - 1 && height[i + 1] <= height[i]) {
                i++;
            }
        }
        if (queue.size() < 2) {
            return 0;
        }
        int[] first = queue.poll(), second = queue.poll();
        int left = Math.min(first[0], second[0]);
        int right = Math.max(first[0], second[0]);
        List<Integer> selectedPoints = new ArrayList<>(Arrays.asList(first[0], second[0]));
        while (!queue.isEmpty()) {
            int[] top = queue.poll();
            if (top[0] >= left && top[0] <= right) {
                continue;
            }
            selectedPoints.add(top[0]);
            left = Math.min(top[0], left);
            right = Math.max(top[0], right);
        }
        Collections.sort(selectedPoints);
        for (int i = 0; i < selectedPoints.size() - 1; i++) {
            int high = Math.min(height[selectedPoints.get(i)], height[selectedPoints.get(i + 1)]);
            for (int j = selectedPoints.get(i); j < selectedPoints.get(i + 1); j++) {
                if (height[j] >= high) {
                    continue;
                }
                sum += high - height[j];
            }
        }
        return sum;
    }
}
