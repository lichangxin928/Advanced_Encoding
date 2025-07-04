package mid;


/**
 * You are given an integer array height of length n. There are n vertical lines drawn such that the two endpoints of the ith line are (i, 0) and (i, height[i]).
 *
 * Find two lines that together with the x-axis form a container, such that the container contains the most water.
 *
 * Return the maximum amount of water a container can store.
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/container-with-most-water
 */
public class ContainerWithMostWater {
    public static void main(String[] args) {
        int [] arr = {2,3,4,5,18,17,6};
        ContainerWithMostWater water = new ContainerWithMostWater();
        int i = water.maxArea(arr);
        System.out.println(i);
    }
    public int maxArea2(int[] height) {
        int left = 0,right = height.length - 1;

        int maxArea = 0;
        if (height[left] > height[right]){
            maxArea = (height.length-1) * height[right];
        }else {
            maxArea = (height.length-1) * height[left];
        }


        for (int i = 0; i < height.length -1 ; i++) {
            if(height[left] > height[right]){
                right--;
                maxArea = getMoreBiggerArea(left,right,height[left],height[right],maxArea );
            }else{
                left++;
                maxArea = getMoreBiggerArea(left,right,height[left],height[right],maxArea );
            }
        }
        return maxArea;

    }
    public int getMoreBiggerArea(int left,int right,int leftHeight,int rightHeight, int OldArea){
        int newArea = 0;
        if(leftHeight > rightHeight){
            newArea = rightHeight * (right - left);
        }else{
            newArea = leftHeight * (right - left);
        }
        if(OldArea > newArea) return OldArea;
        else return newArea;
    }

    public int maxArea(int[] height) {
        int res = 0;
        int left = 0;
        int right = height.length - 1;
        while(left < right){
            int area = getArea(height[left],height[right],right - left);
            if(res < area) res = area;
            if(height[left] > height[right]) right --;
            else left ++;
        }
        return res;
    }

    public static int getArea(int leftHight,int rightHight,int width ){
        return leftHight > rightHight? rightHight * width : leftHight * width;
    }

}
