// Longest Budget-Friendly Streak — variable-size sliding window.
// Checking every possible run is O(n^2). Instead the right end advances one day at a time
// and the left end moves forward only while the total exceeds the budget; a strict-greater
// update keeps the earliest start on ties. Non-negativity is essential: with all costs >= 0,
// shrinking the window from the left can only lower the total, so once the window is valid
// no longer window starting at the same left can fail — with negatives that guarantee breaks
// and the window technique no longer works.
// Complexity: O(n) time (each day enters/leaves once), O(1) extra space.

public class LongestBudgetStreak {
    static String longestStreak(int[] costs, long budget) {
        int bestLength = 0;
        int bestStart = -1;
        long windowSum = 0;
        int left = 0;
        for (int right = 0; right < costs.length; right++) {
            windowSum += costs[right];
            while (windowSum > budget && left <= right) {
                windowSum -= costs[left];
                left++;
            }
            int length = right - left + 1;
            if (windowSum <= budget && length > bestLength) {
                bestLength = length;
                bestStart = left;
            }
        }
        if (bestLength == 0) {
            return "(0, -1)";
        }
        return "(" + bestLength + ", " + bestStart + ")";
    }

    public static void main(String[] args) {
        System.out.println(longestStreak(new int[] { 4, 2, 1, 7, 3, 1, 2, 1, 5 }, 8));
        System.out.println(longestStreak(new int[] { 9, 10 }, 8));
    }
}
