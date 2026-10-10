// Mall Footfall Range Report — prefix sums for O(1) range queries.
// Naive: re-add every value inside each range, O(q * n) for q queries. Optimized: one O(n)
// preprocessing pass builds prefix[i] = sum of visitors[0..i], then each query (start, end)
// is answered as prefix[end] - prefix[start - 1] (start == 0 needs no subtraction).
// Complexity: O(n + q) time, O(n) extra space. The preprocessing pays off whenever many
// queries hit the same day's data — exactly the mall's busy-day scenario.

import java.util.ArrayList;
import java.util.List;

public class MallFootfallReport {
    static List<Integer> footfallReport(int[] visitors, int[][] queries) {
        int[] prefix = new int[visitors.length];
        prefix[0] = visitors[0];
        for (int i = 1; i < visitors.length; i++) {
            prefix[i] = prefix[i - 1] + visitors[i];
        }
        List<Integer> answers = new ArrayList<>();
        for (int[] query : queries) {
            int start = query[0];
            int end = query[1];
            int sum = (start == 0) ? prefix[end] : prefix[end] - prefix[start - 1];
            answers.add(sum);
        }
        return answers;
    }

    public static void main(String[] args) {
        int[] visitors = { 12, 7, 3, 9, 15, 4, 8 };
        int[][] queries = { { 0, 2 }, { 2, 5 }, { 4, 6 }, { 3, 3 } };
        System.out.println(footfallReport(visitors, queries));
    }
}
