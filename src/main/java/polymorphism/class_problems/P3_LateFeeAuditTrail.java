class EventTicket {
    private final double basePrice;
    private double balanceDue;
    private final double[] lateFeeHistory = new double[10];
    private int lateFeeCount;

    public EventTicket(double basePrice) {
        if (basePrice <= 0) {
            throw new IllegalArgumentException("basePrice must be positive");
        }
        this.basePrice = basePrice;
        this.balanceDue = basePrice;
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
       private audit trail. WorkshopTicket's override reuses all of this via super(...). */
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
       ticket's real internal history. */
    double[] getLateFeeHistory() {
        double[] copy = new double[lateFeeCount];
        System.arraycopy(lateFeeHistory, 0, copy, 0, lateFeeCount);
        return copy;
    }
}

/* Workshops have limited seats, so late registrations are penalized double. The override
   calls super.applyLateFee(amount * 2): the parent's deduction logic and its own
   array-recording logic are both reused in one call — no reimplementation, no second,
   separate recording step inside WorkshopTicket. */
class WorkshopTicket extends EventTicket {
    private final String track;

    public WorkshopTicket(double basePrice) {
        this(basePrice, "General");
    }

    public WorkshopTicket(double basePrice, String track) {
        super(basePrice);
        this.track = track;
    }

    public String getTrack() {
        return track;
    }

    @Override
    protected void applyLateFee(double amount) {
        super.applyLateFee(amount * 2);
    }

    public static void main(String[] args) {
        WorkshopTicket w = new WorkshopTicket(1200);
        w.pay(1200);
        w.applyLateFee(100);
        System.out.println(w.getBalanceDue());

        double[] history = w.getLateFeeHistory();
        history[0] = 999;
        System.out.println(java.util.Arrays.toString(w.getLateFeeHistory()));
    }
}