package mid;

import java.util.HashMap;
import java.util.Map;

/**
 * The string "PAYPALISHIRING" is written in a zigzag pattern on a given number of rows like this:
 * (you may want to display this pattern in a fixed font for better legibility)
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/zigzag-conversion
 */
public class ZigZagConversion {

    public static void main(String[] args) {

        String str = "AB";
        ZigZagConversion zig = new ZigZagConversion();
        String convert = zig.convert(str, 1);
        System.out.println(convert);
    }
    public String convert(String s, int numRows) {
        if(s.length()<numRows || numRows == 1){
            return s;
        }
        char arr[] = s.toCharArray();
        int arr_i[] = new int[s.length()];
        char arr_res[] = new char[s.length()];
        int count = 1;
        int flag = 1;
        for(int i = 0;i < s.length();i++){
            if(count > numRows){
                flag *= -1;
                count = numRows - 1;
            }if(count < 1) {
                flag *= -1;
                count = 2;
            }
            if(flag == 1){
                arr_i[i] = count;
                count++;
            }if(flag == -1){
                arr_i[i] = count;
                count--;
            }
        }
        int index = 0;
        for(int i = 1;i <= numRows;i++){
            for(int j = 0;j < s.length();j++){
                if(arr_i[j] == i){
                    arr_res[index] = arr[j];
                    index++;
                }
            }
        }
        return new String(arr_res);
    }
}
