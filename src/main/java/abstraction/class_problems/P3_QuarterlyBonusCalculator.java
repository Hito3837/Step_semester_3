// Quarterly Bonus Calculator — abstract staff, this()/super() chaining, interface check.
// StaffMember's one-argument constructor does nothing but chain via this(baseSalary, 0.10),
// so the default bonus rate lives in exactly one place. TeamLead reaches the parent state
// via super(...). The instanceof check tests against Auditable (not a concrete class), so the
// same method keeps working for any future class that adds the Auditable capability.

abstract class StaffMember {
    private double baseSalary;
    private final double bonusRate;

    public StaffMember(double baseSalary) {
        this(baseSalary, 0.10);
    }

    public StaffMember(double baseSalary, double bonusRate) {
        this.baseSalary = baseSalary;
        this.bonusRate = bonusRate;
    }

    public abstract double calculateBonus();

    double getSalary() {
        return baseSalary;
    }

    void setSalary(double baseSalary) {
        if (baseSalary < 0) {
            System.out.println("Rejected: salary cannot be negative, salary unchanged.");
            return;
        }
        this.baseSalary = baseSalary;
    }

    double getBonusRate() {
        return bonusRate;
    }
}

interface Auditable {
    String auditRecord();
}

class TeamLead extends StaffMember implements Auditable {
    private final int teamSize;

    public TeamLead(double baseSalary, int teamSize) {
        super(baseSalary);
        this.teamSize = teamSize;
    }

    public TeamLead(double baseSalary, double bonusRate, int teamSize) {
        super(baseSalary, bonusRate);
        this.teamSize = teamSize;
    }

    @Override
    public double calculateBonus() {
        return getSalary() * getBonusRate();
    }

    @Override
    public String auditRecord() {
        return "TeamLead audit: " + teamSize + " team members, salary $" + getSalary();
    }
}

public class P3_QuarterlyBonusCalculator {
    static String getAuditIfApplicable(StaffMember s) {
        if (s instanceof Auditable) {
            return ((Auditable) s).auditRecord();
        }
        return "No audit required";
    }

    public static void main(String[] args) {
        TeamLead t = new TeamLead(60000, 5);
        System.out.println(t.calculateBonus());

        TeamLead t2 = new TeamLead(60000, 0.20, 5);
        System.out.println(t2.calculateBonus());

        t.setSalary(-5000);
        System.out.println(t.getSalary());

        StaffMember ref = t; // upcasting: TeamLead stored as its parent type
        System.out.println(getAuditIfApplicable(ref));
    }
}
