class EventTicket {
    private final String attendeeId;
    private final double basePrice;
    private double balanceDue;

    public EventTicket(String attendeeId, double basePrice) {
        if (!isValidId(attendeeId)) {
            throw new IllegalArgumentException("attendeeId must be non-blank and at least 4 characters after trimming");
        }
        if (basePrice <= 0) {
            throw new IllegalArgumentException("basePrice must be positive");
        }
        this.attendeeId = attendeeId.trim();
        this.basePrice = basePrice;
        this.balanceDue = basePrice;
    }

    private static boolean isValidId(String id) {
        if (id == null) {
            return false;
        }
        String trimmed = id.trim();
        return !trimmed.isEmpty() && trimmed.length() >= 4;
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

    /* The base of the family describes itself; every subclass below overrides printTicket(). */
    String printTicket() {
        return "Standard Event Ticket | Balance Due: " + balanceDue;
    }
}

class WorkshopTicket extends EventTicket {
    private final String track;

    public WorkshopTicket(String attendeeId, double basePrice, String track) {
        super(attendeeId, basePrice);
        this.track = track;
    }

    public String getTrack() {
        return track;
    }

    @Override
    String printTicket() {
        return "Workshop Ticket | Track: " + track + " | Balance Due: " + getBalanceDue();
    }
}

/* Multilevel inheritance: PremiumWorkshopTicket -> WorkshopTicket -> EventTicket, three
   classes deep — a regular workshop plus a materials kit. */
class PremiumWorkshopTicket extends WorkshopTicket {
    private final double kitFee;

    public PremiumWorkshopTicket(String attendeeId, double basePrice, String track, double kitFee) {
        super(attendeeId, basePrice, track);
        if (kitFee < 0) {
            throw new IllegalArgumentException("kitFee cannot be negative");
        }
        this.kitFee = kitFee;
    }

    public double getKitFee() {
        return kitFee;
    }

    @Override
    String printTicket() {
        return "Premium Workshop Ticket | Track: " + getTrack() + " | Kit Fee: " + kitFee
                + " | Balance Due: " + getBalanceDue();
    }
}

/* Hierarchical inheritance: HackathonTicket extends the base EventTicket directly, sharing
   nothing with workshops beyond the base ticket itself — an independent branch. */
class HackathonTicket extends EventTicket {
    private final String teamName;

    public HackathonTicket(String attendeeId, double basePrice, String teamName) {
        super(attendeeId, basePrice);
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    @Override
    String printTicket() {
        return "Hackathon Ticket | Team: " + teamName + " | Balance Due: " + getBalanceDue();
    }
}

class TicketFamilyClassifier {
    /* Each shape identifies itself through instanceof alone — no manual "type" field exists
       on any class. Most-derived checks come first so PremiumWorkshopTicket never gets
       swallowed by its own WorkshopTicket ancestor's branch. */
    static String classifyGeneration(EventTicket ticket) {
        if (ticket instanceof PremiumWorkshopTicket) {
            return "Multilevel descendant (3 generations deep)";
        }
        if (ticket instanceof WorkshopTicket) {
            return "Direct subclass (2 generations deep)";
        }
        if (ticket instanceof HackathonTicket) {
            return "Hierarchical sibling (independent branch)";
        }
        return "Base ticket (1 generation)";
    }

    /* A one-line loop body once you trust polymorphism: calling getBalanceDue() on an
       EventTicket reference already runs each object's own version — the method never
       checks a ticket's type before adding its balance in. */
    static double getTotalBalanceDue(EventTicket[] tickets) {
        double total = 0.0;
        for (EventTicket ticket : tickets) {
            if (ticket != null) {
                total += ticket.getBalanceDue();
            }
        }
        return total;
    }

    public static void main(String[] args) {
        EventTicket standardTicket = new EventTicket("STU1", 500);
        WorkshopTicket workshopTicket = new WorkshopTicket("STU2", 1200, "AI/ML");
        PremiumWorkshopTicket premiumTicket = new PremiumWorkshopTicket("STU3", 2000, "Cloud Native", 300);
        HackathonTicket hackathonTicket = new HackathonTicket("STU4", 800, "Byte Force");

        System.out.println(standardTicket.printTicket());
        System.out.println(workshopTicket.printTicket());
        System.out.println(premiumTicket.printTicket());
        System.out.println(hackathonTicket.printTicket());

        System.out.println(classifyGeneration(premiumTicket));
        System.out.println(classifyGeneration(hackathonTicket));
        System.out.println(classifyGeneration(workshopTicket));

        System.out.println(getTotalBalanceDue(new EventTicket[]{
                standardTicket, workshopTicket, premiumTicket, hackathonTicket}));
    }
}