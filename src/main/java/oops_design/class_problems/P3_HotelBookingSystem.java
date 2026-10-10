import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

// Hotel Booking and Cancellation System.
// Classes / relationships / multiplicities:
//   Customer (1) ---- (*) Reservation     : a customer may hold many reservations
//   Room (1) ---- (*) Reservation         : a room may appear in many reservations over time, but NEVER two
//                                           overlapping ACTIVE ones (enforced by BookingManager)
//   Reservation status: ACTIVE -> CANCELLED (one-way; only before the cancellation deadline)
// Pricing is polymorphic: Room delegates to its RoomCategory.ratePerNight(), so a new room category
// needs no change in the booking workflow (Open/Closed).
// Overlap rule: [newIn, newOut) clashes with [in, out) iff newIn < out && in < newOut.
// Cancellation rule: a reservation may be cancelled only when requestDate is strictly before
// checkIn.minusDays(1) (the cancellation deadline).

enum RoomCategory {
    STANDARD(150.0), DELUXE(200.0), SUITE(300.0);

    private final double ratePerNight;

    RoomCategory(double ratePerNight) {
        this.ratePerNight = ratePerNight;
    }

    double ratePerNight() {
        return ratePerNight;
    }
}

enum ReservationStatus {
    ACTIVE, CANCELLED
}

class Room {
    private final String number;
    private final String label;
    private final RoomCategory category;

    Room(String number, String label, RoomCategory category) {
        this.number = number;
        this.label = label;
        this.category = category;
    }

    String getNumber() {
        return number;
    }

    String getLabel() {
        return label;
    }

    RoomCategory getCategory() {
        return category;
    }

    double priceFor(long nights) {
        return category.ratePerNight() * nights;
    }
}

class HotelCustomer {
    private final String name;

    HotelCustomer(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}

class Reservation {
    private static int nextId = 501;
    private final int id;
    private final HotelCustomer customer;
    private final Room room;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private ReservationStatus status = ReservationStatus.ACTIVE;

    Reservation(HotelCustomer customer, Room room, LocalDate checkIn, LocalDate checkOut) {
        this.id = nextId++;
        this.customer = customer;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    int getId() {
        return id;
    }

    Room getRoom() {
        return room;
    }

    LocalDate getCheckIn() {
        return checkIn;
    }

    LocalDate getCheckOut() {
        return checkOut;
    }

    ReservationStatus getStatus() {
        return status;
    }

    LocalDate cancellationDeadline() {
        return checkIn.minusDays(1);
    }

    long nights() {
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    double totalPrice() {
        return room.priceFor(nights());
    }

    boolean overlaps(LocalDate newIn, LocalDate newOut) {
        return newIn.isBefore(checkOut) && checkIn.isBefore(newOut);
    }

    boolean cancel(LocalDate requestDate) {
        if (status == ReservationStatus.CANCELLED) {
            return false;
        }
        if (!requestDate.isBefore(cancellationDeadline())) {
            return false;
        }
        status = ReservationStatus.CANCELLED;
        return true;
    }
}

class BookingManager {
    private final List<Reservation> reservations = new ArrayList<>();

    Reservation book(HotelCustomer customer, Room room, LocalDate checkIn, LocalDate checkOut) {
        if (!checkIn.isBefore(checkOut)) {
            System.out.println("Booking failed: check-in must be before check-out.");
            return null;
        }
        for (Reservation existing : reservations) {
            if (existing.getStatus() == ReservationStatus.ACTIVE
                    && existing.getRoom() == room
                    && existing.overlaps(checkIn, checkOut)) {
                System.out.println("Booking failed: " + room.getLabel() + " is not available for "
                        + checkIn + " to " + checkOut + ".");
                return null;
            }
        }
        Reservation reservation = new Reservation(customer, room, checkIn, checkOut);
        reservations.add(reservation);
        System.out.printf("%s booked from %s to %s. Total price: $%.2f.%n",
                room.getLabel(), checkIn, checkOut, reservation.totalPrice());
        return reservation;
    }

    void cancel(Reservation reservation, LocalDate requestDate) {
        if (reservation == null) {
            return;
        }
        if (reservation.cancel(requestDate)) {
            System.out.println("Reservation for " + reservation.getRoom().getLabel() + " cancelled successfully.");
        } else {
            System.out.println("Cancellation failed: reservation for " + reservation.getRoom().getLabel()
                    + " is past the cancellation deadline (" + reservation.cancellationDeadline() + ").");
        }
    }
}

public class P3_HotelBookingSystem {
    public static void main(String[] args) {
        BookingManager hotel = new BookingManager();
        HotelCustomer customer = new HotelCustomer("Aarav");

        Room deluxe101 = new Room("101", "Deluxe Room 101", RoomCategory.DELUXE);
        Room standard205 = new Room("205", "Standard Room 205", RoomCategory.STANDARD);

        Reservation first = hotel.book(customer, deluxe101,
                LocalDate.of(2024, 12, 1), LocalDate.of(2024, 12, 5));
        hotel.book(customer, standard205,
                LocalDate.of(2024, 12, 3), LocalDate.of(2024, 12, 7));
        hotel.book(customer, deluxe101,
                LocalDate.of(2024, 12, 3), LocalDate.of(2024, 12, 7));
        hotel.cancel(first, LocalDate.of(2024, 11, 28));
    }
}
