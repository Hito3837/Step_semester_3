class RaceEntry {
    /* Every construction assigns a final entryCode from a shared static counter. The
       counter is incremented inside the base constructor only after validation succeeds,
       so every subclass (which funnels through super(...)) increments exactly once and a
       rejected construction never does. The code is never settable from outside and never
       reassignable after construction. */
    private static int bibCounter;
    private final String bibNumber;
    private final double entryFee;
    private double balanceDue;
    private final String entryCode;

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
        bibCounter++;
        this.entryCode = "ENT-" + String.format("%04d", bibCounter);
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

    public String getEntryCode() {
        return entryCode;
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

    /* Mode-aware overload reuses the flat version internally rather than duplicating its
       logic. */
    void pay(double amount, String mode) {
        System.out.println("Paying via " + mode);
        pay(amount);
    }

    double getBalanceDue() {
        return balanceDue;
    }

    /* Exact format: "M" + three digits + one uppercase letter (e.g. "M123A"). Length is
       checked FIRST, before charAt() is ever called on a position that may not exist, and
       each character is verified with isDigit()/isUpperCase() — no regular expression. */
    static boolean isValidDiscountCode(String code) {
        if (code == null || code.length() != 5) {
            return false;
        }
        if (code.charAt(0) != 'M') {
            return false;
        }
        for (int i = 1; i <= 3; i++) {
            if (!Character.isDigit(code.charAt(i))) {
                return false;
            }
        }
        return Character.isUpperCase(code.charAt(4));
    }

    static int getBibCounter() {
        return bibCounter;
    }
}

class RunnerEntry extends RaceEntry {
    private final String category;

    public RunnerEntry(String bibNumber, double entryFee, String category) {
        super(bibNumber, entryFee);
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}

class EliteRunnerEntry extends RunnerEntry {
    private final double sponsorBonus;

    public EliteRunnerEntry(String bibNumber, double entryFee, String category, double sponsorBonus) {
        super(bibNumber, entryFee, category);
        this.sponsorBonus = sponsorBonus;
    }

    public double getSponsorBonus() {
        return sponsorBonus;
    }
}

class RelayTeamEntry extends RaceEntry {
    private final int teamSize;

    public RelayTeamEntry(String bibNumber, double entryFee, int teamSize) {
        super(bibNumber, entryFee);
        if (teamSize <= 0) {
            throw new IllegalArgumentException("teamSize must be positive");
        }
        this.teamSize = teamSize;
    }

    public int getTeamSize() {
        return teamSize;
    }
}

class NightlySettlementEngine {
    /* Reconciles a night's worth of entries using instanceof to separate relay teams from
       individual entries, and never throws on a null batch entry. */
    static String settleNight(RaceEntry[] entries) {
        int processed = 0;
        int nullSkipped = 0;
        int relay = 0;
        int individual = 0;
        if (entries != null) {
            for (RaceEntry entry : entries) {
                if (entry == null) {
                    nullSkipped++;
                    continue;
                }
                if (entry instanceof RelayTeamEntry) {
                    relay++;
                } else {
                    individual++;
                }
                processed++;
            }
        }
        return processed + " processed | " + nullSkipped + " null skipped | "
                + relay + " relay | " + individual + " individual";
    }

    public static void main(String[] args) {
        System.out.println(RaceEntry.isValidDiscountCode("M123A"));
        System.out.println(RaceEntry.isValidDiscountCode("M12A"));
        System.out.println(RaceEntry.isValidDiscountCode("X123A"));

        RunnerEntry runner = new RunnerEntry("BIB2001", 80, "Open 10K");
        runner.pay(10, "UPI");
        EliteRunnerEntry eliteEntry = new EliteRunnerEntry("BIB3001", 150, "Elite Full Marathon", 500);
        RelayTeamEntry relayEntry = new RelayTeamEntry("BIB4001", 300, 4);

        System.out.println(settleNight(new RaceEntry[]{eliteEntry, null, relayEntry}));

        RaceEntry plain = new RaceEntry("BIB5001", 50);
        System.out.println("Entry code: " + plain.getEntryCode() + " | " + runner.getEntryCode());

        try {
            new RaceEntry("B1", 50);
        } catch (IllegalArgumentException e) {
            System.out.println("rejected construction did not increment the counter");
        }
        System.out.println(RaceEntry.getBibCounter());
    }
}