// Net-Balance Period Counter — prefix sums plus a frequency map.
// Brute force sums every subarray: O(n^2). Key insight: if the running total up to day j is S,
// a period ending at j sums to k exactly when an earlier running total equalled S - k — so one
// pass counting how often each running total has appeared finds every match. A plain sliding
// window cannot work here because negatives let a sum drop back to k after overshooting.
// Complexity: O(n) time, O(n) extra space for the frequency map (long sums avoid overflow).

import java.util.HashMap;
import java.util.Map;

public class NetBalancePeriodCounter {
    static long countPeriods(int[] transactions, long k) {
        Map<Long, Long> seen = new HashMap<>();
        seen.put(0L, 1L);
        long running = 0;
        long count = 0;
        for (int value : transactions) {
            running += value;
            count += seen.getOrDefault(running - k, 0L);
            seen.put(running, seen.getOrDefault(running, 0L) + 1);
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(countPeriods(new int[] { 3, 4, -7, 1, 3, 3, 1, -4 }, 7));
        System.out.println(countPeriods(new int[] { 1, 2, 3 }, 10));
    }
}
