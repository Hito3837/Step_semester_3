class EventTicket {
    /* Every construction receives a unique, auto-incrementing ticketId from a shared static
       counter, incremented once per object and stored in a public final field: never settable
       from outside and never reassignable after construction. */
    private static int ticketCounter;
    private final double basePrice;
    private double balanceDue;
    public final String ticketId;

    public EventTicket(double basePrice) {
        if (basePrice <= 0) {
            throw new IllegalArgumentException("basePrice must be positive");
        }
        this.basePrice = basePrice;
        this.balanceDue = basePrice;
        ticketCounter++;
        this.ticketId = "TCK-" + (1000 + ticketCounter);
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

    /* Mode-aware overload only adds a printed mode before delegating to the one-argument
       version — both payments land on the same underlying balance. */
    void pay(double amount, String mode) {
        System.out.println("Paying via " + mode);
        pay(amount);
    }

    double getBalanceDue() {
        return balanceDue;
    }

    /* Exact format: "F" + three digits + one uppercase letter (e.g. "F123A"). Wrong length
       fails immediately, before charAt() is ever called on a position that may not exist,
       and each remaining character is verified with isDigit()/isUpperCase() — no regex. */
    static boolean isValidPromoCode(String code) {
        if (code == null || code.length() != 5) {
            return false;
        }
        if (code.charAt(0) != 'F') {
            return false;
        }
        for (int i = 1; i <= 3; i++) {
            if (!Character.isDigit(code.charAt(i))) {
                return false;
            }
        }
        return Character.isUpperCase(code.charAt(4));
    }

    static int getTicketsIssued() {
        return ticketCounter;
    }
}

/* Group-booking variant: shares everything with the base ticket, just carries a team size. */
class GroupTicket extends EventTicket {
    private final int groupSize;

    public GroupTicket(double basePrice, int groupSize) {
        super(basePrice);
        if (groupSize <= 0) {
            throw new IllegalArgumentException("groupSize must be positive");
        }
        this.groupSize = groupSize;
    }

    public int getGroupSize() {
        return groupSize;
    }
}

class NightlySettlementTool {
    /* Reconciles a night's worth of tickets using instanceof to separate group bookings from
       regular individual tickets, and never throws on a null batch entry. */
    static String processNightlySettlement(EventTicket[] tickets) {
        int processed = 0;
        int nullSkipped = 0;
        int group = 0;
        int individual = 0;
        if (tickets != null) {
            for (EventTicket ticket : tickets) {
                if (ticket == null) {
                    nullSkipped++;
                    continue;
                }
                if (ticket instanceof GroupTicket) {
                    group++;
                } else {
                    individual++;
                }
                processed++;
            }
        }
        return processed + " processed | " + nullSkipped + " null skipped | "
                + group + " group | " + individual + " individual";
    }

    public static void main(String[] args) {
        EventTicket t1 = new EventTicket(500);
        System.out.println(t1.ticketId);
        System.out.println(EventTicket.getTicketsIssued());

        System.out.println(EventTicket.isValidPromoCode("F123A"));
        System.out.println(EventTicket.isValidPromoCode("F12A"));
        System.out.println(EventTicket.isValidPromoCode("X123A"));

        t1.pay(200);
        t1.pay(200, "UPI");
        System.out.println(t1.getBalanceDue());

        System.out.println(processNightlySettlement(new EventTicket[]{
                new GroupTicket(2000, 5),
                null,
                new EventTicket(500)
        }));
    }
}