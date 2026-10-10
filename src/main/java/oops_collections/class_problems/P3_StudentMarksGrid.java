// Student Marks Grid — ragged 2D marks with per-student totals and per-subject toppers.
// A ragged int[][] models missing marks naturally: Ravi's row simply has no Eng entry, so no
// sentinel value can ever leak into a total or a topper comparison. Totals sum only the row's
// own entries; each subject's topper scans only the rows that actually hold that column.

public class P3_StudentMarksGrid {
    public static void main(String[] args) {
        String[] subjects = { "Math", "Sci", "Eng" };
        String[] students = { "Anu", "Ravi", "Meena" };
        int[][] marks = { { 80, 90, 70 }, { 60, 85 }, { 95, 75, 88 } };

        StringBuilder totals = new StringBuilder("Totals");
        for (int s = 0; s < students.length; s++) {
            int total = 0;
            for (int mark : marks[s]) {
                total += mark;
            }
            totals.append(" ").append(students[s]).append(" ").append(total).append(",");
        }
        totals.setLength(totals.length() - 1);
        System.out.println(totals);

        StringBuilder toppers = new StringBuilder("toppers");
        for (int sub = 0; sub < subjects.length; sub++) {
            String topper = null;
            int best = Integer.MIN_VALUE;
            for (int s = 0; s < students.length; s++) {
                if (sub < marks[s].length && marks[s][sub] > best) {
                    best = marks[s][sub];
                    topper = students[s];
                }
            }
            toppers.append(" ").append(subjects[sub]).append(" ").append(topper)
                    .append(" ").append(best).append(",");
        }
        toppers.setLength(toppers.length() - 1);
        System.out.println(toppers);
    }
}
