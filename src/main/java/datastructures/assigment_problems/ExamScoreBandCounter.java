// Exam Score Band Counter — two binary searches instead of a linear scan.
// A linear scan is O(n) per query. A standard binary search stops at ANY match, which is
// useless with duplicates at the band edges — so two lower-bound searches are used: the first
// index with score >= low and the first index with score > high; the count is their difference.
// Complexity: O(log n) time per query, O(1) extra space.

public class ExamScoreBandCounter {
    // First index with scores[index] >= value (scores.length if none).
    static int lowerBound(int[] scores, int value) {
        int low = 0;
        int high = scores.length;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (scores[mid] < value) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    static int countInBand(int[] scores, int low, int high) {
        return lowerBound(scores, high + 1) - lowerBound(scores, low);
    }

    public static void main(String[] args) {
        int[] scores = { 35, 42, 42, 50, 58, 58, 58, 63, 71, 88 };
        System.out.println(countInBand(scores, 42, 58));
        System.out.println(countInBand(scores, 90, 100));
    }
}
