package mid;

public class LongestSubstring {

    /**
     * Given a string s, find the length of the longest substring without repeating characters.
     *
     *
     * 滑动窗口
     * @param s
     * @return 返回最长的不重复的子串
     */
    public static int lengthOfLongestSubstring(String s) {
        int[] flags = new int[128];
        for (int i = 0; i < flags.length; i++) {
            flags[i] = -1;
        }

        int start = 0;
        int res = 0;
        for (int i = 0; i < s.length(); i++) {
            int index = s.charAt(i);
            // 这里必须是 >= start - 1 来判断这个字符的上一次出现是在 start 的后面
            if (flags[index] >= start - 1){
                start = flags[index] + 1;
            }
            res = Math.max(res,i - start + 1);
            flags[index] = i;
        }

        return res;
    }

    public static void main(String[] args) {

        int a = lengthOfLongestSubstring("abcabcbb");
        System.out.println(a);
    }
}
