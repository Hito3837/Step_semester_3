import java.util.ArrayList;
import java.util.List;

class BusTicket {
    private final String passengerName;
    private final String destination;
    private boolean checkedIn;

    /* No usable no-arg constructor: an invalid booking can never be constructed. */
    public BusTicket(String passengerName, String destination) {
        if (!isMeaningful(passengerName)) {
            throw new IllegalArgumentException("passengerName must be a meaningful name");
        }
        if (!isMeaningful(destination)) {
            throw new IllegalArgumentException("destination must be a meaningful place name");
        }
        this.passengerName = passengerName.trim();
        this.destination = destination.trim();
        this.checkedIn = false;
    }

    /* Blank, whitespace-only and null all fail, and a name is invalid not only when it is
       missing: digits/symbols ("Ravi123") also make it meaningless for this platform. */
    static boolean isMeaningful(String value) {
        if (value == null) {
            return false;
        }
        String v = value.trim();
        if (v.isEmpty()) {
            return false;
        }
        for (int i = 0; i < v.length(); i++) {
            char c = v.charAt(i);
            if (!Character.isLetter(c) && !Character.isWhitespace(c) && c != '-' && c != '.') {
                return false;
            }
        }
        return true;
    }

    void markCheckedIn() {
        if (checkedIn) {
            System.out.println("WARNING: duplicate check-in blocked - ticket already checked in");
        } else {
            checkedIn = true;
            System.out.println("Checked in");
        }
    }

    static void processBatch(String[][] rawBookings) {
        int valid = 0;
        int rejected = 0;
        int duplicates = 0;
        List<String> acceptedPairs = new ArrayList<>();
        for (String[] booking : rawBookings) {
            try {
                BusTicket ticket = new BusTicket(booking[0], booking[1]);
                String key = ticket.passengerName.toLowerCase() + "|" + ticket.destination.toLowerCase();
                if (acceptedPairs.contains(key)) {
                    duplicates++;
                } else {
                    acceptedPairs.add(key);
                    valid++;
                }
            } catch (IllegalArgumentException e) {
                rejected++;
            }
        }
        System.out.println("Valid: " + valid + " | Rejected: " + rejected
                + " | Duplicates skipped: " + duplicates);
    }

    public static void main(String[] args) {
        processBatch(new String[][]{
                {"Divya", "Chennai"},
                {"", "Bangalore"},
                {"Ravi123", "Pune"},
                {"Divya", "Chennai"},
                {"  ", "   "}
        });
        new BusTicket("Divya", "Chennai").markCheckedIn();
    }
}