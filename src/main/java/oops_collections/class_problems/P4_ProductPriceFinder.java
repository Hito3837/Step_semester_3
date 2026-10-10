// Product Price Finder — binary-search bounds on a sorted price list.
// Scanning is O(n); two lower-bound searches are O(log n) each. lowerBound(low) is the first
// index with price >= low, and lowerBound(high + 1) doubles as the exclusive upper bound —
// the difference is the inclusive count, duplicates at the edges included automatically.

public class P4_ProductPriceFinder {
    // First index with prices[index] >= value (prices.length if none).
    static int lowerBound(int[] prices, int value) {
        int low = 0;
        int high = prices.length;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (prices[mid] < value) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    public static void main(String[] args) {
        int[] prices = { 100, 150, 150, 200, 300, 450 };
        int low = 150;
        int high = 300;

        int lower = lowerBound(prices, low);
        int upper = lowerBound(prices, high + 1);
        System.out.println("Lower bound index " + lower);
        System.out.println("Upper bound index " + upper);
        System.out.print("Count " + (upper - lower) + " (products with prices ");
        for (int i = lower; i < upper; i++) {
            if (i > lower) {
                System.out.print(", ");
            }
            System.out.print(prices[i]);
        }
        System.out.println(")");
    }
}
