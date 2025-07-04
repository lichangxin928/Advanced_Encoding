package hard;


/**
 * Given an input string sand a pattern p, implement regular expression matching with support for '.' and '*' where:
 *
 * '.' Matches any single character.
 * '*' Matches zero or more of the preceding element.
 * The matching should cover the entire input string (not partial).
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/regular-expression-matching
 */
public class RegularExpressionMatching {

    public static void main(String[] args) {
        String str = "abb";
        String par = "a*";

        RegularExpressionMatching matching = new RegularExpressionMatching();
        boolean result = matching.isMatch(str, par);
        System.out.println(result);
    }

    public boolean isMatch(String s, String p) {

        char []s_arr = s.toCharArray();
        char []p_arr = p.toCharArray();

        int s_index = 0;
        int p_index = 0;

        for (int i = 0; i < p_arr.length; i++) {

            // 正则表达式为 .* 的情况时，一定返回true
            if(p_arr[p_index] == '.' && p_index < p_arr.length && p_arr[p_index + 1] == '*' && s_arr.length < s_index) return true;

            if(s_arr[s_index] != '.' && s_arr[s_index] != '*'){
                if (s_arr[s_index] != p_arr[p_index]) {
                    return false;
                }
                p_index ++;
                s_index ++;
            }

            if(p_arr[p_index] == '.' && s_index < s_arr.length && p_index <p_arr.length) {
                p_index ++;
                s_index ++;
            }

            if(p_arr[p_index] == '*'){

            }
        }

        return true;
    }
}
