package mid;

import algorithm.sort.QuickSort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

//给你一个整数数组 arr 和一个整数 k 。现需要从数组中恰好移除 k 个元素，请找出移除后数组中不同整数的最少数目。
//
//
//
//
//
//
// 示例 1：
//
// 输入：arr = [5,5,4], k = 1
//输出：1
//解释：移除 1 个 4 ，数组中只剩下 5 一种整数。
//
//
// 示例 2：
//
// 输入：arr = [4,3,1,1,3,3,2], k = 3
//输出：2
//解释：先移除 4、2 ，然后再移除两个 1 中的任意 1 个或者三个 3 中的任意 1 个，最后剩下 1 和 3 两种整数。
//
//
//
// 提示：
//
//
// 1 <= arr.length <= 10^5
// 1 <= arr[i] <= 10^9
// 0 <= k <= arr.length
//
//
// Related Topics 贪心 数组 哈希表 计数 排序 👍 106 👎 0
public class FindLeastNumOfUniqueInts {
    public static void main(String[] args) {
        System.out.println(findLeastNumOfUniqueInts(new int[]{1}, 1));
    }
    public static int findLeastNumOfUniqueInts(int[] arr, int k) {


        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            if(map.containsKey(arr[i])) {
                map.put(arr[i], map.get(arr[i])+1);
            } else {
                map.put(arr[i], 1);
            }
        }
        List<Integer> countList = map.entrySet().stream()
                .map(Map.Entry::getValue)
                .sorted()
                .collect(Collectors.toList());
        if(k == 0 || k == arr.length) {
            return countList.size() - k;
        }
        int i;
        for ( i = 0; i < countList.size() && k >= 0; i++) {
            k-=countList.get(i);
        }
        return countList.size()-i+1;
    }
}
