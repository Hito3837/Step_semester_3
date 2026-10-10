import java.util.*;

// The SwiftShip Parcel Tracker — booking, guarded status flow, polymorphic charges, fan-out notifications.
// Classes / relationships / multiplicities:
//   ShipCustomer (1) ---- (*) Parcel        : a customer may book many parcels
//   Parcel (1) ---- (*) NotifyChannel       : every subscribed channel is notified on each status change
//   ShippingRule <|-- StandardShipping, ExpressShipping; FragileShipping reuses StandardShipping
//                through COMPOSITION (has-a standard rule + handling fee), not inheritance
//   NotifyChannel <|-- SmsChannel, EmailChannel (interface; new channels plug in freely)
// Status machine BOOKED -> PICKED_UP -> IN_TRANSIT -> OUT_FOR_DELIVERY -> DELIVERED lives inside
// Parcel, so no outside code can skip a step or move backwards; cancel is allowed only from BOOKED.
// Charges are polymorphic: ParcelService asks the ShippingRule, never the concrete type, so new
// shipping types need no change in the status-update workflow.

enum ParcelStatus {
    BOOKED, PICKED_UP, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, CANCELLED
}

interface ShippingRule {
    String typeName();

    double chargeFor(double weightKg);
}

class StandardShipping implements ShippingRule {
    @Override
    public String typeName() {
        return "Standard";
    }

    @Override
    public double chargeFor(double weightKg) {
        return 40.0 + 10.0 * weightKg;
    }
}

class ExpressShipping implements ShippingRule {
    @Override
    public String typeName() {
        return "Express";
    }

    @Override
    public double chargeFor(double weightKg) {
        return 80.0 + 15.0 * weightKg;
    }
}

class FragileShipping implements ShippingRule {
    private final StandardShipping standard = new StandardShipping();

    @Override
    public String typeName() {
        return "Fragile";
    }

    @Override
    public double chargeFor(double weightKg) {
        return standard.chargeFor(weightKg) + 50.0;
    }
}

interface NotifyChannel {
    void send(String parcelId, ParcelStatus status);
}

class SmsChannel implements NotifyChannel {
    @Override
    public void send(String parcelId, ParcelStatus status) {
        System.out.println("[SMS] " + parcelId + " is now " + status + ".");
    }
}

class EmailChannel implements NotifyChannel {
    @Override
    public void send(String parcelId, ParcelStatus status) {
        System.out.println("[Email] " + parcelId + " is now " + status + ".");
    }
}

class ShipCustomer {
    private final String name;

    ShipCustomer(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}

class Parcel {
    private static final Map<ParcelStatus, ParcelStatus> NEXT = new EnumMap<>(ParcelStatus.class);

    static {
        NEXT.put(ParcelStatus.BOOKED, ParcelStatus.PICKED_UP);
        NEXT.put(ParcelStatus.PICKED_UP, ParcelStatus.IN_TRANSIT);
        NEXT.put(ParcelStatus.IN_TRANSIT, ParcelStatus.OUT_FOR_DELIVERY);
        NEXT.put(ParcelStatus.OUT_FOR_DELIVERY, ParcelStatus.DELIVERED);
    }

    private final String id;
    private final ShippingRule shipping;
    private final double weightKg;
    private final double charge;
    private final List<NotifyChannel> channels = new ArrayList<>();
    private ParcelStatus status = ParcelStatus.BOOKED;

    Parcel(String id, ShippingRule shipping, double weightKg, double charge) {
        this.id = id;
        this.shipping = shipping;
        this.weightKg = weightKg;
        this.charge = charge;
    }

    String getId() {
        return id;
    }

    ParcelStatus getStatus() {
        return status;
    }

    void subscribe(NotifyChannel channel) {
        channels.add(channel);
    }

    void announce() {
        for (NotifyChannel channel : channels) {
            channel.send(id, status);
        }
    }

    void advanceTo(ParcelStatus next) {
        if (NEXT.get(status) == next) {
            status = next;
            announce();
        } else {
            System.out.println("Invalid transition: " + status + " \u2192 " + next + " is not allowed.");
        }
    }

    void cancel() {
        if (status != ParcelStatus.BOOKED) {
            System.out.println("Cancellation failed: " + id + " can be cancelled only while BOOKED.");
            return;
        }
        status = ParcelStatus.CANCELLED;
        announce();
    }

    static String weightText(double weightKg) {
        if (weightKg == Math.floor(weightKg)) {
            return String.valueOf((long) weightKg);
        }
        return String.valueOf(weightKg);
    }
}

class ParcelService {
    Parcel book(ShipCustomer customer, String parcelId, ShippingRule shipping,
            double weightKg, List<NotifyChannel> channels) {
        if (weightKg <= 0) {
            System.out.println("Booking failed: weight must be positive.");
            return null;
        }
        Parcel parcel = new Parcel(parcelId, shipping, weightKg, shipping.chargeFor(weightKg));
        for (NotifyChannel channel : channels) {
            parcel.subscribe(channel);
        }
        System.out.printf("Parcel %s booked (%s, %s kg). Charge: \u20B9%.2f.%n",
                parcelId, shipping.typeName(), Parcel.weightText(weightKg),
                shipping.chargeFor(weightKg));
        parcel.announce();
        return parcel;
    }
}

public class SwiftShipParcelTracker {
    public static void main(String[] args) {
        // Force UTF-8 bytes for ₹/→ regardless of the platform default charset.
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        ParcelService service = new ParcelService();
        ShipCustomer customer = new ShipCustomer("Aarav");

        Parcel p101 = service.book(customer, "P101", new ExpressShipping(), 2.0,
                Arrays.asList(new SmsChannel(), new EmailChannel()));
        p101.advanceTo(ParcelStatus.PICKED_UP);
        p101.cancel();
        p101.advanceTo(ParcelStatus.IN_TRANSIT);
        p101.advanceTo(ParcelStatus.DELIVERED);
    }
}
