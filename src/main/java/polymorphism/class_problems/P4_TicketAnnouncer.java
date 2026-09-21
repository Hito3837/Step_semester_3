class EventTicket {
    private final double basePrice;
    private double balanceDue;

    public EventTicket(double basePrice) {
        if (basePrice <= 0) {
            throw new IllegalArgumentException("basePrice must be positive");
        }
        this.basePrice = basePrice;
        this.balanceDue = basePrice;
    }

    double getBalanceDue() {
        return balanceDue;
    }

    /* Short board-line form; a plain ticket has no extra detail to announce. */
    String printTicket() {
        return "Standard | Balance: " + balanceDue;
    }
}

class WorkshopTicket extends EventTicket {
    private final String track;

    public WorkshopTicket(double basePrice, String track) {
        super(basePrice);
        this.track = track;
    }

    public String getTrack() {
        return track;
    }

    @Override
    String printTicket() {
        return "Workshop | Track: " + track + " | Balance: " + getBalanceDue();
    }
}

class TicketAnnouncer {
    /* Reads out every ticket by calling printTicket() polymorphically over an EventTicket[]
       — no instanceof-based if-else chain decides what to print. The whole report is
       assembled with ONE StringBuilder appended to across every loop iteration, never a
       new one per ticket and never repeated String concatenation. */
    static String batchPrint(EventTicket[] tickets) {
        StringBuilder sb = new StringBuilder();
        for (EventTicket ticket : tickets) {
            sb.append(ticket.printTicket());
            /* For anything genuinely a WorkshopTicket, guard a downcast to reach the
               workshop-specific track. Check first, then cast, only inside the branch where
               the check already passed — never attempt-and-catch the ClassCastException. */
            if (ticket instanceof WorkshopTicket) {
                WorkshopTicket workshop = (WorkshopTicket) ticket;
                sb.append(" [Track via downcast: ").append(workshop.getTrack()).append("]");
            }
            sb.append(" | ");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(batchPrint(new EventTicket[]{
                new EventTicket(500),
                new WorkshopTicket(1200, "AI/ML")
        }));

        /* This compiles fine — the compiler only checks that WorkshopTicket is some kind of
           EventTicket — but plain is genuinely not a WorkshopTicket at runtime, so the cast
           fails the moment it executes. That is exactly why the instanceof guard is required
           instead of an attempt-and-catch. */
        EventTicket plain = new EventTicket(500);
        try {
            WorkshopTicket bad = (WorkshopTicket) plain;
            System.out.println(bad.getTrack());
        } catch (ClassCastException e) {
            System.out.println("ClassCastException at runtime");
        }
    }
}