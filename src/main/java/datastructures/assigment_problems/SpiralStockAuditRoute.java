// Spiral Stock Audit Route — clockwise spiral with four shrinking boundaries.
// Walk top row, right column, bottom row, left column, then shrink inward. The boundary-cross
// checks before the bottom row and left column are essential: with a single remaining row or
// column they would otherwise be walked twice.
// Complexity: O(m * n) time visiting every bin once, O(1) additional space (output excluded).

import java.util.ArrayList;
import java.util.List;

public class SpiralStockAuditRoute {
    static List<Integer> auditRoute(int[][] grid) {
        List<Integer> route = new ArrayList<>();
        int top = 0;
        int bottom = grid.length - 1;
        int left = 0;
        int right = grid[0].length - 1;
        while (top <= bottom && left <= right) {
            for (int col = left; col <= right; col++) {
                route.add(grid[top][col]);
            }
            top++;
            for (int row = top; row <= bottom; row++) {
                route.add(grid[row][right]);
            }
            right--;
            if (top <= bottom) {
                for (int col = right; col >= left; col--) {
                    route.add(grid[bottom][col]);
                }
                bottom--;
            }
            if (left <= right) {
                for (int row = bottom; row >= top; row--) {
                    route.add(grid[row][left]);
                }
                left++;
            }
        }
        return route;
    }

    public static void main(String[] args) {
        int[][] grid = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 } };
        System.out.println(auditRoute(grid));
    }
}
