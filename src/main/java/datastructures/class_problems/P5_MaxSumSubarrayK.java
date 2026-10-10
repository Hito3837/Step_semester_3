// Maximum Sum Subarray of Fixed Size K — sliding window without recomputation.
// The naive approach re-adds all k elements for each of the (n - k + 1) windows: O(n * k).
// Instead the window slides one step at a time: subtract the element leaving on the left,
// add the element entering on the right — each element is touched at most twice overall.
// Complexity: O(n) time, O(1) extra space.

import java.util.Arrays;

class MaxWindow {
    final int sum;
    final int start;

    MaxWindow(int sum, int start) {
        this.sum = sum;
        this.start = start;
    }
}

public class P5_MaxSumSubarrayK {
    static MaxWindow maxSumSubarray(int[] sales, int k) {
        int windowSum = 0;
        for (int i = 0; i < k; i++) {
            windowSum += sales[i];
        }
        int best = windowSum;
        int bestStart = 0;
        for (int end = k; end < sales.length; end++) {
            windowSum += sales[end] - sales[end - k];
            if (windowSum > best) {
                best = windowSum;
                bestStart = end - k + 1;
            }
        }
        return new MaxWindow(best, bestStart);
    }

    public static void main(String[] args) {
        int[] sales = { 2, 1, 5, 1, 3, 2 };
        int k = 3;
        MaxWindow answer = maxSumSubarray(sales, k);
        System.out.println(answer.sum + " (from the subarray "
                + Arrays.toString(Arrays.copyOfRange(sales, answer.start, answer.start + k)) + ")");
    }
}
