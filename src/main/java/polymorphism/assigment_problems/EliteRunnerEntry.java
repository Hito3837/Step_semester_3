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

    /* The base of the family describes itself; every subclass below overrides announce(). */
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
    String announce() {
        return "Runner Entry | Bib: " + getBibNumber() + " | Category: " + category
                + " | Balance: " + getBalanceDue();
    }
}

/* Multilevel inheritance: EliteRunnerEntry -> RunnerEntry -> RaceEntry, three classes deep. */
class EliteRunnerEntry extends RunnerEntry {
    private final double sponsorBonus;

    public EliteRunnerEntry(String bibNumber, double entryFee, String category, double sponsorBonus) {
        super(bibNumber, entryFee, category);
        this.sponsorBonus = sponsorBonus;
    }

    public double getSponsorBonus() {
        return sponsorBonus;
    }

    @Override
    String announce() {
        return "Elite Runner | Bib: " + getBibNumber()
                + " | Category: " + getCategory()
                + " | Sponsor Bonus: " + sponsorBonus
                + " | Balance: " + getBalanceDue();
    }
}

/* Hierarchical inheritance: RelayTeamEntry extends the base RaceEntry directly and shares
   nothing with RunnerEntry beyond the base entry itself — an independent branch. */
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

class FamilyClassifier {
    /* Each subclass identifies itself through instanceof alone — no manual "type" field
       exists on any class. Most-derived checks come first so a subclass never gets
       swallowed by its own ancestor's branch. */
    static String classifyGeneration(RaceEntry entry) {
        if (entry instanceof EliteRunnerEntry) {
            return "Multilevel descendant (3 generations deep)";
        }
        if (entry instanceof RunnerEntry) {
            return "Direct subclass (2 generations deep)";
        }
        if (entry instanceof RelayTeamEntry) {
            return "Hierarchical sibling (independent branch)";
        }
        return "Base entry (1 generation)";
    }

    /* A one-line loop body once you trust polymorphism: calling getBalanceDue() on a
       RaceEntry reference already runs each object's own version — the method never checks
       an entry's type before adding its balance in. */
    static double getTotalBalanceDue(RaceEntry[] entries) {
        double total = 0.0;
        for (RaceEntry entry : entries) {
            if (entry != null) {
                total += entry.getBalanceDue();
            }
        }
        return total;
    }

    public static void main(String[] args) {
        RunnerEntry runnerEntry = new RunnerEntry("BIB2001", 80, "Open 10K");
        EliteRunnerEntry eliteEntry = new EliteRunnerEntry("BIB3001", 150, "Elite Full Marathon", 500);
        RelayTeamEntry relayEntry = new RelayTeamEntry("BIB4001", 300, 4);

        System.out.println(runnerEntry.announce());
        System.out.println(eliteEntry.announce());
        System.out.println(relayEntry.announce());

        System.out.println(classifyGeneration(eliteEntry));
        System.out.println(classifyGeneration(relayEntry));
        System.out.println(classifyGeneration(runnerEntry));

        System.out.println(getTotalBalanceDue(new RaceEntry[]{runnerEntry, eliteEntry, relayEntry}));
    }
}