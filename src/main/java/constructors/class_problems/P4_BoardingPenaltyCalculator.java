/* final class + final field + final method: whoever uses this calculator must not be able
   to override the rule, mutate the configured floor, or extend the class. */
final class BoardingPenaltyCalculator {
    private final double minimumPenaltyPercent;

    public BoardingPenaltyCalculator(double minimumPenaltyPercent) {
        if (minimumPenaltyPercent < 0) {
            throw new IllegalArgumentException("minimumPenaltyPercent cannot be negative");
        }
        this.minimumPenaltyPercent = minimumPenaltyPercent;
    }

    /* Rejects bad input at the point of calculation, not just at construction.
       Never applies the flat floor when the passenger boards on time (minutesLate == 0). */
    final double calculatePenalty(double ticketFare, int minutesLate) {
        if (ticketFare < 0 || minutesLate < 0) {
            throw new IllegalArgumentException("ticketFare and minutesLate cannot be negative");
        }
        if (minutesLate == 0) {
            return 0.0;
        }
        double percent = 0.0;
        percent += Math.min(5, minutesLate) * 0.5;
        percent += Math.max(0, Math.min(15, minutesLate) - 5) * 1.0;
        percent += Math.max(0, minutesLate - 15) * 2.0;
        double tiered = ticketFare * percent / 100.0;
        double floor = ticketFare * minimumPenaltyPercent / 100.0;
        return Math.max(tiered, floor);
    }

    public static void main(String[] args) {
        BoardingPenaltyCalculator calc = new BoardingPenaltyCalculator(1.0);
        System.out.println("Rs " + calc.calculatePenalty(1000, 0));
        System.out.println("Rs " + calc.calculatePenalty(1000, 1));
        System.out.println("Rs " + calc.calculatePenalty(1000, 16));
    }
}