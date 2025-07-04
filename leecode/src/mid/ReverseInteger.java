package mid;

/**
 * Given a signed 32-bit integer x, return x with its digits reversed. If reversing x causes the value to go outside the signed 32-bit integer range [-231, 231 - 1], then return 0.
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/reverse-integer
 *
 * 注意点就是边界问题，当x为 0 时的情况，还有就是超出int类型的情况的时候
 */
public class ReverseInteger {
    public static void main(String[] args) {
        ReverseInteger integer = new ReverseInteger();
        int reverse = integer.reverse(
                1534236469);
        System.out.println(reverse);
    }

    public int reverse(int x) {
        if(x == 0){
            return 0;
        }
        long res = 0;
        int temp = 1;
        int count = 0;
        boolean flag = true;
        // 去除x最后面的零
        while(flag){
            if(x % 10 == 0){
                x = x/10;
            }else{
                flag = false;
            }
        }
        for(int i = x;i != 0;i/=10){
            count++;
        }
        for(int i = 0;i < count - 1;i++){
            temp *=10;
        }

        for(int i = x;x != 0;x/=10){
            res += (x%10) * temp;
            temp /=10;
        }
        return (int)res==res? (int)res:0;
    }
    public int reverse_sim(int x) {
        long n = 0;
        while(x != 0) {
            n = n*10 + x%10;
            x = x/10;
        }
        return (int)n==n ? (int)n:0;
    }
}
