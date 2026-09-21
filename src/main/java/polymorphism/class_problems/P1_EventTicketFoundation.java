class EventTicket {
    /* Shared foundation with a validated constructor — the only place the attendee-ID rule
       lives, so WorkshopTicket (and any future subclass) inherits it via super(...) instead
       of duplicating attendeeId/basePrice or re-checking the rule. */
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

    public String getAttendeeId() {
        return attendeeId;
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

    /* Let EventTicket's constructor be the only place the validation rule lives: try to
       build one, catch the IllegalArgumentException the rejection throws, and count it —
       never pre-validate the strings here. One bad ID must not crash the whole desk's
       submission. */
    static String registerBatch(String[] attendeeIds, double basePrice) {
        int registered = 0;
        int rejected = 0;
        for (String id : attendeeIds) {
            try {
                new EventTicket(id, basePrice);
                registered++;
            } catch (IllegalArgumentException e) {
                rejected++;
            }
        }
        return "Registered: " + registered + " | Rejected: " + rejected;
    }

    public static void main(String[] args) {
        try {
            new EventTicket("ST1", 500);
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected");
        }

        WorkshopTicket w = new WorkshopTicket("STU2", 1200, "AI/ML");
        w.pay(500);
        System.out.println(w.getBalanceDue());

        System.out.println(registerBatch(new String[]{"STU1", "ST1", "STU2", "   ", "STU3"}, 500));
    }
}

/* Single-inheritance specialization: forwards the shared fields via super(...) — attendeeId
   and basePrice are never re-declared here. */
class WorkshopTicket extends EventTicket {
    private final String track;

    public WorkshopTicket(String attendeeId, double basePrice, String track) {
        super(attendeeId, basePrice);
        this.track = track;
    }

    public String getTrack() {
        return track;
    }
}