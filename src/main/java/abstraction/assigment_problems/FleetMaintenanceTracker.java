// Fleet Maintenance Tracker — multilevel hierarchy plus an orthogonal capability.
// ServiceableVehicle -> Forklift -> HeavyDutyForklift is three levels deep: HeavyDutyForklift
// never restates Forklift's steps, it calls super.performMaintenance() and appends only its
// own hydraulic-pressure check. Insurable is a separate concern from servicing, so it is an
// interface — and the instanceof check tests the interface, working for any future machine
// that gains the capability, not just Forklift.

abstract class ServiceableVehicle {
    private double mileage;

    public abstract String performMaintenance();

    double getMileage() {
        return mileage;
    }

    void addMileage(double km) {
        if (km < 0) {
            System.out.println("Rejected: distance cannot be negative, mileage unchanged.");
            return;
        }
        mileage += km;
    }
}

interface Insurable {
    String getInsuranceInfo();
}

class Forklift extends ServiceableVehicle implements Insurable {
    private final String assetTag;

    public Forklift(String assetTag) {
        super();
        this.assetTag = assetTag;
    }

    @Override
    public String performMaintenance() {
        return "Forklift " + assetTag + ": hydraulic and fork inspection complete";
    }

    @Override
    public String getInsuranceInfo() {
        return "Insured under fleet policy - Asset " + assetTag;
    }
}

class HeavyDutyForklift extends Forklift {
    public HeavyDutyForklift(String assetTag) {
        super(assetTag);
    }

    @Override
    public String performMaintenance() {
        return super.performMaintenance() + " | high-pressure hydraulic check complete";
    }
}

public class FleetMaintenanceTracker {
    static String getInsuranceIfApplicable(ServiceableVehicle v) {
        if (v instanceof Insurable) {
            return ((Insurable) v).getInsuranceInfo();
        }
        return "No insurance record exists";
    }

    public static void main(String[] args) {
        Forklift f = new Forklift("FL-22");
        f.addMileage(120);
        System.out.println(f.getMileage());
        System.out.println(f.performMaintenance());

        HeavyDutyForklift hd = new HeavyDutyForklift("HD-9");
        System.out.println(hd.performMaintenance());

        System.out.println(getInsuranceIfApplicable(f));
        System.out.println(getInsuranceIfApplicable(hd));
    }
}
