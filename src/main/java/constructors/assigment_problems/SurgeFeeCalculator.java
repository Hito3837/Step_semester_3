final class SurgeFeeCalculator {
    private final double minimumSurgePercent;

    SurgeFeeCalculator(double minimumSurgePercent) {
        if (minimumSurgePercent < 0) {
            throw new IllegalArgumentException("minimumSurgePercent cannot be negative");
        }
        this.minimumSurgePercent = minimumSurgePercent;
    }

    final double calculateSurgeFee(double orderValue, int delayMinutes) {
        if (orderValue < 0 || delayMinutes < 0) {
            throw new IllegalArgumentException("orderValue and delayMinutes cannot be negative");
        }
        if (delayMinutes == 0) {
            return 0.0;
        }
        double percent = 0.0;
        percent += Math.min(delayMinutes, 5) * 0.5;
        percent += Math.max(0, Math.min(delayMinutes, 15) - 5) * 1.0;
        percent += Math.max(0, delayMinutes - 15) * 2.0;
        double tiered = orderValue * percent / 100.0;
        double floor = orderValue * minimumSurgePercent / 100.0;
        return Math.max(tiered, floor);
    }

    public static void main(String[] args) {
        SurgeFeeCalculator calc = new SurgeFeeCalculator(1.0);
        System.out.println("Rs " + calc.calculateSurgeFee(500, 0));
        System.out.println("Rs " + calc.calculateSurgeFee(500, 1));
        System.out.println("Rs " + calc.calculateSurgeFee(500, 16));
    }
}