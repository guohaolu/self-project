package org.example.hash;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 解决问题
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] nums = solution.twoSum(new int[]{2, 7, 7, 11, 15}, 9);
        System.out.println(Arrays.toString(nums));
    }

    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> storeMap = Arrays.stream(strs).collect(Collectors.groupingBy(str -> {
            if (str.isEmpty()) {
                return str;
            }
            char[] charArray = str.toCharArray();
            Arrays.sort(charArray);
            return new String(charArray);
        }));
        return new ArrayList<>(storeMap.values());
    }

    public int[] twoSum(int[] nums, int target) {
        Map<Integer, List<Integer>> valueIdxMap = IntStream.range(0, nums.length).boxed().collect(Collectors.groupingBy(i -> nums[i]));
        for (Map.Entry<Integer, List<Integer>> entry : valueIdxMap.entrySet()) {
            if (!valueIdxMap.containsKey(target - entry.getKey())) {
                continue;
            }
            List<Integer> matchableIdxList = valueIdxMap.get(target - entry.getKey());
            int matchableIdx = entry.getKey() == target - entry.getKey() ? matchableIdxList.get(1) : matchableIdxList.get(0);
            return new int[]{entry.getValue().get(0), matchableIdx};
        }
        return null;
    }

    public int longestConsecutive(int[] nums) {
        int longest = 0;
        Set<Integer> storeSet = Arrays.stream(nums).boxed().collect(Collectors.toSet());
        for (int num : nums) {
            if (!storeSet.contains(num)) {
                continue;
            }
            int curLength = 1;
            for (int i = num - 1; ; i--) {
                if (storeSet.contains(i)) {
                    curLength++;
                    storeSet.remove(i);
                    continue;
                }
                break;
            }
            for (int j = num + 1; ; j++) {
                if (storeSet.contains(j)) {
                    curLength++;
                    storeSet.remove(j);
                    continue;
                }
                break;
            }
            longest = Math.max(curLength, longest);
        }
        return longest;
    }
}

