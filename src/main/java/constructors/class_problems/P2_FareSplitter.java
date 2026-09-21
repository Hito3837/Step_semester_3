import java.util.Arrays;

class FareSplitter {
    private final String tripId;
    private final double totalFare;
    private final int passengerCount;

    public FareSplitter(String tripId, double totalFare, int passengerCount) {
        if (tripId == null || tripId.trim().isEmpty()) {
            throw new IllegalArgumentException("tripId cannot be blank");
        }
        if (totalFare < 0) {
            throw new IllegalArgumentException("totalFare cannot be negative");
        }
        if (passengerCount <= 0) {
            throw new IllegalArgumentException("passengerCount must be positive");
        }
        this.tripId = tripId.trim();
        this.totalFare = totalFare;
        this.passengerCount = passengerCount;
    }

    public FareSplitter(String tripId, double totalFare) {
        this(tripId, totalFare, 2);
    }

    public FareSplitter(String tripId) {
        this(tripId, 0.0, 2);
    }

    /* Fair split in paisa (integer math) so no money is ever lost to rounding.
       The remainder goes to the LAST share: all earlier shares are equal, and the
       last passenger absorbs the leftover paisa. */
    double[] fareBreakdown() {
        double[] shares = new double[passengerCount];
        long totalPaisa = Math.round(totalFare * 100);
        long base = totalPaisa / passengerCount;
        long remainder = totalPaisa % passengerCount;
        for (int i = 0; i < passengerCount; i++) {
            shares[i] = base / 100.0;
        }
        shares[passengerCount - 1] += remainder / 100.0;
        return shares;
    }

    boolean isConfirmationOverdue(int confirmed, int expected) {
        if (expected <= 0) {
            throw new IllegalArgumentException("expected must be positive");
        }
        if (confirmed < 0 || confirmed > expected) {
            throw new IllegalArgumentException("confirmed must be between 0 and expected");
        }
        return confirmed < expected;
    }

    static void printBreakdown(FareSplitter splitter) {
        double[] shares = splitter.fareBreakdown();
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < shares.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(String.format("%.2f", shares[i]));
        }
        sb.append("]");
        System.out.println(sb);
    }

    public static void main(String[] args) {
        FareSplitter uneven = new FareSplitter("TRIP001", 100000, 3);
        printBreakdown(uneven);
        System.out.println("Sum: " + String.format("%.2f", Arrays.stream(uneven.fareBreakdown()).sum()));

        printBreakdown(new FareSplitter("TRIP003"));

        FareSplitter small = new FareSplitter("TRIP004", 1000, 3);
        printBreakdown(small);
        System.out.println("Sum: " + String.format("%.2f", Arrays.stream(small.fareBreakdown()).sum()));

        FareSplitter trip = new FareSplitter("TRIP009", 5000, 4);
        System.out.println("Overdue (1 confirmed of 4): " + trip.isConfirmationOverdue(1, 4));
    }
}