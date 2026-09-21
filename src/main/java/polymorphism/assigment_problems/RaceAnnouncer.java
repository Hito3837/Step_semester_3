class RaceEntry {
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

    protected void applyLateFee(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount cannot be negative");
        }
        balanceDue += amount;
    }

    String announce() {
        return "Race Entry | Bib: " + bibNumber + " | Balance: " + balanceDue;
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

    @Override
    protected void applyLateFee(double amount) {
        super.applyLateFee(amount * 2);
    }

    @Override
    String announce() {
        return "Runner Entry | Bib: " + getBibNumber() + " | Category: " + category
                + " | Balance: " + getBalanceDue();
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

    @Override
    String announce() {
        return "Relay Team | Bib: " + getBibNumber() + " | Team Size: " + teamSize
                + " | Balance: " + getBalanceDue();
    }
}

class RaceAnnouncer {
    /* Reads out every entry by calling announce() polymorphically over a RaceEntry[] — no
       instanceof-based if-else chain decides what to print. The whole report is assembled
       with ONE StringBuilder appended to across every loop iteration, never a new one per
       entry and never repeated String concatenation. */
    static String announceAll(RaceEntry[] entries) {
        StringBuilder sb = new StringBuilder();
        for (RaceEntry entry : entries) {
            sb.append(entry.announce());
            /* For anything genuinely a RelayTeamEntry, guard a downcast to reach the
               relay-specific team size. Check first, then cast, only inside the branch where
               the check already passed — never attempt-and-catch the ClassCastException. */
            if (entry instanceof RelayTeamEntry) {
                RelayTeamEntry relay = (RelayTeamEntry) entry;
                sb.append(" [Team size via downcast: ").append(relay.getTeamSize()).append("]");
            }
            sb.append(" | ");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        RunnerEntry runnerEntry = new RunnerEntry("BIB2001", 80, "Open 10K");
        runnerEntry.pay(30);
        runnerEntry.applyLateFee(20);
        RelayTeamEntry relayEntry = new RelayTeamEntry("BIB4001", 300, 4);

        System.out.println(announceAll(new RaceEntry[]{runnerEntry, relayEntry}));

        /* A plain RaceEntry is genuinely not a RelayTeamEntry at runtime, so an unchecked
           downcast would fail the moment it executes — exactly why the instanceof guard is
           required instead of an attempt-and-catch. */
        RaceEntry plain = new RaceEntry("BIB5001", 50);
        System.out.println(plain.announce());
    }
}