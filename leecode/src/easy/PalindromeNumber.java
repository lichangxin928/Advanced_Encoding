package easy;

/**
 * Given an integer x, return true if x is palindrome integer.
 *
 * An integer is a palindrome when it reads the same backward as forward.
 *
 */
public class PalindromeNumber {
    public boolean isPalindrome(int x) {
        return  new StringBuffer(x+"").reverse().toString().equals(x+"");
    }
}
