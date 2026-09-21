class RaceEntry {
    private final String bibNumber;
    private final double entryFee;
    private double balanceDue;
    private final double[] lateFeeHistory = new double[10];
    private int lateFeeCount;

    public RaceEntry(String bibNumber, double entryFee) {
        if (!isValidBib(bibNumber)) {
            throw new IllegalArgumentException("bibNumber must be non-blank and at least 4 characters after trimming");
        }
        if (entryFee <= 0) {
            throw new IllegalArgumentException("entryFee must be positive");
        }
        this.bibNumber = bibNumber.trim();
        this.entryFee = entryFee;
        this.balanceDue = entryFee;
    }

    private static boolean isValidBib(String bib) {
        if (bib == null) {
            return false;
        }
        String trimmed = bib.trim();
        return !trimmed.isEmpty() && trimmed.length() >= 4;
    }

    public String getBibNumber() {
        return bibNumber;
    }

    void pay(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount cannot be negative");
        }
        balanceDue -= amount;
        if (balanceDue < 0) {
            balanceDue = 0.0;
        }
    }

    double getBalanceDue() {
        return balanceDue;
    }

    /* The base penalty: push the balance up and record the exact amount applied into the
       private audit trail. RunnerEntry's override reuses all of this via super(...). */
    protected void applyLateFee(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount cannot be negative");
        }
        balanceDue += amount;
        if (lateFeeCount < lateFeeHistory.length) {
            lateFeeHistory[lateFeeCount++] = amount;
        }
    }

    /* Defensive copy every time — tampering with the returned array never reaches the
       entry's real internal history. */
    double[] getLateFeeHistory() {
        double[] copy = new double[lateFeeCount];
        System.arraycopy(lateFeeHistory, 0, copy, 0, lateFeeCount);
        return copy;
    }
}

/* Runner spots are limited, so a late runner faces double the standard penalty. The
   override calls super.applyLateFee(amount * 2): the parent's deduction logic and its own
   array-recording logic are both reused in one call — no reimplementation, and no second,
   separate recording step. */
class RunnerEntry extends RaceEntry {
    private final String category;

    public RunnerEntry(String bibNumber, double entryFee, String category) {
        super(bibNumber, entryFee);
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    @Override
    protected void applyLateFee(double amount) {
        super.applyLateFee(amount * 2);
    }

    public static void main(String[] args) {
        RunnerEntry r = new RunnerEntry("BIB2001", 80, "Open 10K");
        r.pay(30);
        r.applyLateFee(20);
        System.out.println(r.getBalanceDue());

        double[] history = r.getLateFeeHistory();
        history[0] = 999;
        System.out.println(java.util.Arrays.toString(r.getLateFeeHistory()));
    }
}