class RaceEntry {
    /* Shared foundation for the marathon entry system: a validated constructor is the only
       place the bib-number rule lives, so RunnerEntry and any future subclass inherit it via
       super(...) instead of duplicating the fields or the check. */
    private final String bibNumber;
    private final double entryFee;
    private double balanceDue;

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

    public double getEntryFee() {
        return entryFee;
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

    /* Let RaceEntry's constructor be the only place the validation rule lives: try to build
       one, catch the IllegalArgumentException the rejection throws, and count it — never
       pre-validate the strings here. One bad bib must not crash the kiosk's submission. */
    static String registerBatch(String[] bibNumbers, double entryFee) {
        int registered = 0;
        int rejected = 0;
        for (String bib : bibNumbers) {
            try {
                new RaceEntry(bib, entryFee);
                registered++;
            } catch (IllegalArgumentException e) {
                rejected++;
            }
        }
        return "Registered: " + registered + " | Rejected: " + rejected;
    }

    public static void main(String[] args) {
        try {
            new RaceEntry("B1", 50);
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected");
        }

        RunnerEntry r = new RunnerEntry("BIB2001", 80, "Open 10K");
        r.pay(30);
        System.out.println(r.getBalanceDue());

        System.out.println(registerBatch(new String[]{"BIB1", "B1", "BIB2"}, 80));
    }
}

/* Single-inheritance specialization: forwards the shared fields via super(...) — bibNumber
   and entryFee are never re-declared here. */
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