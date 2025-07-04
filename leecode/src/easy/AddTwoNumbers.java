package easy;


public class AddTwoNumbers {
    /**
     * You are given two non-empty linked lists representing two non-negative integers. The digits are stored in reverse order,
     * and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.
     *
     * You may assume the two numbers do not contain any leading zero, except the number 0 itself.
     *
     * @param l1
     * @param l2
     * @return
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        int value = 0;
        int temp = 0;
        ListNode resNode = null;
        ListNode footNode = null;
        while (l1 != null || l2 != null){
            if(l1 != null && l2 != null){
                value = l1.val + l2.val + temp;
                temp = 0;
                if(value >= 10){
                    value -= 10;
                    temp = 1;
                }
                l1 = l1.next;
                l2 = l2.next;
            }else if(l1 != null && l2 == null){
                value = l1.val + temp;
                temp = 0;
                if(value >= 10){
                    value -= 10;
                    temp = 1;
                }
                l1 = l1.next;
            }else{
                value = l2.val + temp;
                temp = 0;
                if(value >= 10){
                    value -= 10;
                    temp = 1;
                }
                l2 = l2.next;
            }
            if(resNode == null){
                resNode = new ListNode(value);
                footNode = resNode;
            }else{
                footNode.next = new ListNode(value);
                footNode = footNode.next;
            }
        }
        if(temp == 1){
            footNode.next = new ListNode(1);
        }
        return resNode;
    }
}


class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}
