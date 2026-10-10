// Pair Sum in a Sorted Array — two pointers exploiting sorted order.
// One pointer starts at each end: if the sum is too small the left (smaller) value must grow,
// if too big the right (larger) value must shrink, so every step discards one element that can
// never be part of the answer. Each index moves at most once.
// Complexity: O(n) time, O(1) extra space. A brute-force nested loop ignoring the sorted
// property checks every pair: O(n^2) time (a hash-set variant is O(n) time but O(n) space).

public class P1_PairSumSorted {
    static String pairSumSorted(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int sum = nums[left] + nums[right];
            if (sum == target) {
                return "(" + nums[left] + ", " + nums[right] + ")";
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        return "Not Found";
    }

    public static void main(String[] args) {
        System.out.println(pairSumSorted(new int[] { -4, -1, 0, 3, 5, 9 }, 4));
        System.out.println(pairSumSorted(new int[] { 1, 2, 3 }, 100));
    }
}
