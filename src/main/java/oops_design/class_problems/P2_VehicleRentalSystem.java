import java.util.*;

// Vehicle Rental System with dynamic (polymorphic) pricing.
// Classes / relationships / multiplicities:
//   Customer (1) ---- (*) Rental        : a customer may hold many rentals, each rental belongs to one customer
//   Vehicle (1) ---- (0..1) Rental      : a vehicle has at most one ACTIVE rental (never double-booked)
//   Vehicle <|-- StandardCar, LuxuryCar, SuvCar (inheritance; new categories plug in via ratePerDay())
//   RentalService orchestrates rent / charge / return.
// Pricing is polymorphic: RentalService calls vehicle.ratePerDay() without knowing the concrete
// category, so a new vehicle category needs no change in the rental workflow (Open/Closed).
// Availability: Vehicle.available flips false on rent and true on return; rent() refuses a
// vehicle that already has an active rental.

abstract class Vehicle {
    private final String name;
    private boolean available = true;

    Vehicle(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    boolean isAvailable() {
        return available;
    }

    void setAvailable(boolean available) {
        this.available = available;
    }

    abstract double ratePerDay();

    abstract String getCategory();
}

class StandardCar extends Vehicle {
    StandardCar(String name) {
        super(name);
    }

    @Override
    double ratePerDay() {
        return 50.0;
    }

    @Override
    String getCategory() {
        return "Standard";
    }
}

class LuxuryCar extends Vehicle {
    LuxuryCar(String name) {
        super(name);
    }

    @Override
    double ratePerDay() {
        return 100.0;
    }

    @Override
    String getCategory() {
        return "Luxury";
    }
}

class SuvCar extends Vehicle {
    SuvCar(String name) {
        super(name);
    }

    @Override
    double ratePerDay() {
        return 75.0;
    }

    @Override
    String getCategory() {
        return "SUV";
    }
}

class Customer {
    private final String name;

    Customer(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}

class Rental {
    private final Customer customer;
    private final Vehicle vehicle;
    private final int days;
    private final double totalCharge;
    private boolean active = true;

    Rental(Customer customer, Vehicle vehicle, int days) {
        this.customer = customer;
        this.vehicle = vehicle;
        this.days = days;
        this.totalCharge = vehicle.ratePerDay() * days;
    }

    Customer getCustomer() {
        return customer;
    }

    Vehicle getVehicle() {
        return vehicle;
    }

    int getDays() {
        return days;
    }

    double getTotalCharge() {
        return totalCharge;
    }

    boolean isActive() {
        return active;
    }

    void close() {
        active = false;
    }
}

class RentalService {
    private final List<Rental> rentals = new ArrayList<>();

    Rental rent(Customer customer, Vehicle vehicle, int days) {
        if (!vehicle.isAvailable()) {
            System.out.println(vehicle.getName() + " cannot be rented: it already has an active rental.");
            return null;
        }
        if (days <= 0) {
            System.out.println("Rental duration must be at least one day.");
            return null;
        }
        Rental rental = new Rental(customer, vehicle, days);
        vehicle.setAvailable(false);
        rentals.add(rental);
        System.out.printf("%s rented for %d days. Total charge: $%.2f.%n",
                vehicle.getName(), days, rental.getTotalCharge());
        return rental;
    }

    void returnVehicle(Vehicle vehicle) {
        for (Rental rental : rentals) {
            if (rental.getVehicle() == vehicle && rental.isActive()) {
                rental.close();
                vehicle.setAvailable(true);
                System.out.println(vehicle.getName() + " returned. Now available.");
                return;
            }
        }
        System.out.println(vehicle.getName() + " has no active rental to return.");
    }
}

public class P2_VehicleRentalSystem {
    public static void main(String[] args) {
        RentalService service = new RentalService();
        Customer customer = new Customer("Aarav");

        Vehicle luxuryCarA = new LuxuryCar("Luxury Car A");
        Vehicle standardCarB = new StandardCar("Standard Car B");

        service.rent(customer, luxuryCarA, 3);
        service.rent(customer, standardCarB, 5);
        service.returnVehicle(luxuryCarA);
    }
}
