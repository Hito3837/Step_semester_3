class BusTicketAccount {
    static int processed;
    static int skipped;
    static int sleeperCount;
    static int regularCount;
    static double grandTotal;

    /* One-time, shared class-level setup runs exactly once when this class is first loaded. */
    private static final double MINIMUM_PENALTY_PERCENT;

    static {
        MINIMUM_PENALTY_PERCENT = 1.0;
        processed = 0;
        skipped = 0;
        sleeperCount = 0;
        regularCount = 0;
        grandTotal = 0.0;
    }

    private final String bookingId;
    private final double ticketFare;

    public BusTicketAccount(String bookingId, double ticketFare) {
        if (bookingId == null || bookingId.trim().isEmpty()) {
            throw new IllegalArgumentException("bookingId cannot be blank");
        }
        if (ticketFare < 0) {
            throw new IllegalArgumentException("ticketFare cannot be negative");
        }
        this.bookingId = bookingId.trim();
        this.ticketFare = ticketFare;
    }

    public BusTicketAccount(String bookingId) {
        this(bookingId, 0.0);
    }

    /* final because the settlement rule must not be overridable. This is the same tiered
       formula as Problem 4's BoardingPenaltyCalculator (inlined so this file is standalone),
       with the floor configured to 1%. */
    final double calculatePenalty(int minutesLate) {
        if (minutesLate < 0) {
            throw new IllegalArgumentException("minutesLate cannot be negative");
        }
        if (minutesLate == 0) {
            return 0.0;
        }
        double percent = 0.0;
        percent += Math.min(5, minutesLate) * 0.5;
        percent += Math.max(0, Math.min(15, minutesLate) - 5) * 1.0;
        percent += Math.max(0, minutesLate - 15) * 2.0;
        double tiered = ticketFare * percent / 100.0;
        double floor = ticketFare * MINIMUM_PENALTY_PERCENT / 100.0;
        return Math.max(tiered, floor);
    }

    void processAccount(BusTicketAccount account, double amount, int minutesLate) {
        if (account == null) {
            skipped++;
            return;
        }
        double penalty = account.calculatePenalty(minutesLate);
        if (account instanceof SleeperBusTicketAccount) {
            penalty *= 0.5;
            sleeperCount++;
        } else {
            regularCount++;
        }
        grandTotal += penalty;
        processed++;
    }

    static void processBatch(BusTicketAccount[] accounts, double[] amounts, int[] minutesLateArray) {
        /* Fail before anything runs if the parallel arrays don't line up: a depot manager
           would far rather see an error immediately than a batch that quietly charges the
           wrong passenger. */
        if (accounts.length != amounts.length || accounts.length != minutesLateArray.length) {
            throw new IllegalArgumentException("accounts, amounts and minutesLateArray must share the same length");
        }
        processed = 0;
        skipped = 0;
        sleeperCount = 0;
        regularCount = 0;
        grandTotal = 0.0;
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i] == null) {
                skipped++;
                continue;
            }
            accounts[i].processAccount(accounts[i], amounts[i], minutesLateArray[i]);
        }
        System.out.println(processed + " processed | " + skipped + " null skipped | "
                + sleeperCount + " sleeper | " + regularCount + " regular | "
                + "grand total penalties = Rs " + grandTotal);
    }

    public static void main(String[] args) {
        BusTicketAccount[] accounts = {
                new SleeperBusTicketAccount("BK001", 2000),
                null,
                new BusTicketAccount("BK002", 1200)
        };
        double[] amounts = {1200, 900, 700};
        int[] minutesLateArray = {10, 5, 0};
        processBatch(accounts, amounts, minutesLateArray);
    }
}

class SleeperBusTicketAccount extends BusTicketAccount {
    SleeperBusTicketAccount(String bookingId, double ticketFare) {
        super(bookingId, ticketFare);
    }

    SleeperBusTicketAccount(String bookingId) {
        super(bookingId);
    }
}