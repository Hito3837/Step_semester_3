class DeliveryAccount {
    static int processed;
    static int skipped;
    static int premiumCount;
    static int regularCount;
    static double grandTotal;

    private static final SurgeFeeCalculator SURGE;

    static {
        SURGE = new SurgeFeeCalculator(1.0);
        processed = 0;
        skipped = 0;
        premiumCount = 0;
        regularCount = 0;
        grandTotal = 0.0;
    }

    private final String studentId;
    private final double orderValue;

    public DeliveryAccount(String studentId, double orderValue) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("studentId cannot be blank");
        }
        if (orderValue < 0) {
            throw new IllegalArgumentException("orderValue cannot be negative");
        }
        this.studentId = studentId.trim();
        this.orderValue = orderValue;
    }

    public DeliveryAccount(String studentId) {
        this(studentId, 0.0);
    }

    final double calculateSurgeFee(int delayMinutes) {
        return SURGE.calculateSurgeFee(orderValue, delayMinutes);
    }

    static double surgeFeeOn(double amount, int delayMinutes) {
        return SURGE.calculateSurgeFee(amount, delayMinutes);
    }

    void processAccount(DeliveryAccount account, double amount, int delayMinutes) {
        if (account == null) {
            skipped++;
            return;
        }
        double fee = surgeFeeOn(amount, delayMinutes);
        if (account instanceof PremiumAccount) {
            fee *= 0.5;
            premiumCount++;
        } else {
            regularCount++;
        }
        grandTotal += fee;
        processed++;
    }

    static void processBatch(DeliveryAccount[] accounts, double[] amounts, int[] delayMinutesArray) {
        processed = 0;
        skipped = 0;
        premiumCount = 0;
        regularCount = 0;
        grandTotal = 0.0;

        if (accounts.length != amounts.length || accounts.length != delayMinutesArray.length) {
            throw new IllegalArgumentException("accounts, amounts and delayMinutesArray must share the same length");
        }

        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i] == null) {
                skipped++;
                continue;
            }
            accounts[i].processAccount(accounts[i], amounts[i], delayMinutesArray[i]);
        }

        System.out.println(processed + " processed | " + skipped + " null skipped | "
                + premiumCount + " premium | " + regularCount + " regular | "
                + "grand total surge fees = Rs " + grandTotal);
    }

    static class PremiumAccount extends DeliveryAccount {
        PremiumAccount(String studentId, double orderValue) {
            super(studentId, orderValue);
        }

        PremiumAccount(String studentId) {
            super(studentId);
        }
    }

    public static void main(String[] args) {
        DeliveryAccount[] accounts = new DeliveryAccount[]{
                new PremiumAccount("STU001", 500),
                null,
                new DeliveryAccount("STU002", 300)
        };
        double[] amounts = {500, 400, 300};
        int[] delays = {10, 5, 0};
        processBatch(accounts, amounts, delays);
    }
}