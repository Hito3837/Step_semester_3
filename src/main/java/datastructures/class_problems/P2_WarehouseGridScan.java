// Warehouse Bin Grid Scan — single traversal with running total and first-max tracking.
// The grid is scanned once row by row, left to right; the maximum uses a strict-greater-than
// update, so ties automatically keep the FIRST maximum encountered, as required.
// Complexity: O(m * n) time visiting every bin once, O(1) additional space (input excluded).

class BinSummary {
    final long total;
    final int maxRow;
    final int maxCol;

    BinSummary(long total, int maxRow, int maxCol) {
        this.total = total;
        this.maxRow = maxRow;
        this.maxCol = maxCol;
    }

    @Override
    public String toString() {
        return "total = " + total + ", maxCoordinate = (" + maxRow + ", " + maxCol + ")";
    }
}

public class P2_WarehouseGridScan {
    static BinSummary warehouseSummary(int[][] grid) {
        long total = 0;
        int bestValue = Integer.MIN_VALUE;
        int bestRow = 0;
        int bestCol = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                total += grid[row][col];
                if (grid[row][col] > bestValue) {
                    bestValue = grid[row][col];
                    bestRow = row;
                    bestCol = col;
                }
            }
        }
        return new BinSummary(total, bestRow, bestCol);
    }

    public static void main(String[] args) {
        int[][] grid = { { 4, 9, 2 }, { 7, 1, 6 }, { 3, 12, 5 } };
        System.out.println(warehouseSummary(grid));
    }
}
