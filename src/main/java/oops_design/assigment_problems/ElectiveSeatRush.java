import java.util.*;

// The Elective Seat Rush — capacity, credit limits, waitlist, atomic drop-and-promote.
// Classes / relationships / multiplicities:
//   EnrollStudent (*) ---- (*) Elective      : many-to-many; an elective holds at most `capacity`
//                                            enrolled students (invariant, always protected)
//   EnrollStudent <|-- RegularStudent, HonorsStudent, ExchangeStudent (credit limit is polymorphic:
//                    24 / 28 / 20, so new student types plug in with no workflow change)
//   Elective (1) ---- (*) Enrollment        : enrolments live and die with the elective
//   The waitlist is a private FIFO queue inside Elective — invisible to outsiders.
// Rules: duplicate enrol/wait is refused; the credit limit is checked BEFORE seat availability;
// on drop, the first ELIGIBLE waitlisted student (credit re-checked) is promoted in the same
// operation, so enrolled count never exceeds capacity and the queue order is always honoured.

abstract class EnrollStudent {
    private final String name;
    private int currentCredits;

    EnrollStudent(String name, int currentCredits) {
        this.name = name;
        this.currentCredits = currentCredits;
    }

    String getName() {
        return name;
    }

    int getCurrentCredits() {
        return currentCredits;
    }

    void addCredits(int credits) {
        currentCredits += credits;
    }

    void dropCredits(int credits) {
        currentCredits -= credits;
    }

    abstract String typeName();

    abstract int creditLimit();
}

class RegularStudent extends EnrollStudent {
    RegularStudent(String name, int currentCredits) {
        super(name, currentCredits);
    }

    @Override
    String typeName() {
        return "Regular";
    }

    @Override
    int creditLimit() {
        return 24;
    }
}

class HonorsStudent extends EnrollStudent {
    HonorsStudent(String name, int currentCredits) {
        super(name, currentCredits);
    }

    @Override
    String typeName() {
        return "Honors";
    }

    @Override
    int creditLimit() {
        return 28;
    }
}

class ExchangeStudent extends EnrollStudent {
    ExchangeStudent(String name, int currentCredits) {
        super(name, currentCredits);
    }

    @Override
    String typeName() {
        return "Exchange";
    }

    @Override
    int creditLimit() {
        return 20;
    }
}

class Enrollment {
    private final EnrollStudent student;
    private final Elective elective;

    Enrollment(EnrollStudent student, Elective elective) {
        this.student = student;
        this.elective = elective;
    }

    EnrollStudent getStudent() {
        return student;
    }
}

class Elective {
    private final String name;
    private final int creditValue;
    private final int capacity;
    private final List<Enrollment> enrolled = new ArrayList<>();
    private final Queue<EnrollStudent> waitlist = new LinkedList<>();

    Elective(String name, int creditValue, int capacity) {
        this.name = name;
        this.creditValue = creditValue;
        this.capacity = capacity;
    }

    String getName() {
        return name;
    }

    int getCreditValue() {
        return creditValue;
    }

    private boolean withinLimit(EnrollStudent student) {
        return student.getCurrentCredits() + creditValue <= student.creditLimit();
    }

    private boolean isEnrolled(EnrollStudent student) {
        for (Enrollment enrollment : enrolled) {
            if (enrollment.getStudent() == student) {
                return true;
            }
        }
        return false;
    }

    void enroll(EnrollStudent student) {
        if (isEnrolled(student) || waitlist.contains(student)) {
            System.out.println("Enrollment failed: " + student.getName()
                    + " is already enrolled in or waiting for " + name + ".");
            return;
        }
        if (!withinLimit(student)) {
            System.out.println("Enrollment failed: " + student.getName() + " would exceed the "
                    + student.typeName() + " credit limit ("
                    + (student.getCurrentCredits() + creditValue) + "/" + student.creditLimit() + ").");
            return;
        }
        if (enrolled.size() >= capacity) {
            waitlist.add(student);
            System.out.println(name + " is full. " + student.getName()
                    + " added to waitlist (position " + waitlist.size() + ").");
            return;
        }
        place(student);
    }

    private void place(EnrollStudent student) {
        student.addCredits(creditValue);
        enrolled.add(new Enrollment(student, this));
        System.out.println(student.getName() + " enrolled in " + name + " (credits: "
                + student.getCurrentCredits() + "/" + student.creditLimit() + ").");
    }

    void drop(EnrollStudent student) {
        Enrollment found = null;
        for (Enrollment enrollment : enrolled) {
            if (enrollment.getStudent() == student) {
                found = enrollment;
                break;
            }
        }
        if (found == null) {
            System.out.println("Drop failed: " + student.getName() + " is not enrolled in " + name + ".");
            return;
        }
        enrolled.remove(found);
        student.dropCredits(creditValue);
        System.out.println(student.getName() + " dropped " + name + " (credits: "
                + student.getCurrentCredits() + "/" + student.creditLimit() + ").");
        promoteFromWaitlist();
    }

    // Drop-and-promote is one consistent operation: the freed seat goes to the first
    // waitlisted student who still fits their credit limit; skipped students keep their order.
    private void promoteFromWaitlist() {
        Iterator<EnrollStudent> it = waitlist.iterator();
        while (it.hasNext()) {
            EnrollStudent candidate = it.next();
            if (withinLimit(candidate)) {
                it.remove();
                candidate.addCredits(creditValue);
                enrolled.add(new Enrollment(candidate, this));
                System.out.println(candidate.getName()
                        + " promoted from waitlist and enrolled in " + name + " (credits: "
                        + candidate.getCurrentCredits() + "/" + candidate.creditLimit() + ").");
                return;
            }
        }
    }
}

class EnrollmentService {
    void enroll(EnrollStudent student, Elective elective) {
        elective.enroll(student);
    }

    void drop(EnrollStudent student, Elective elective) {
        elective.drop(student);
    }
}

public class ElectiveSeatRush {
    public static void main(String[] args) {
        EnrollmentService service = new EnrollmentService();
        Elective cloud = new Elective("Cloud Computing", 4, 2);

        EnrollStudent asha = new RegularStudent("Asha", 20);
        EnrollStudent ravi = new HonorsStudent("Ravi", 22);
        EnrollStudent neha = new ExchangeStudent("Neha", 12);
        EnrollStudent kiran = new RegularStudent("Kiran", 22);

        service.enroll(asha, cloud);
        service.enroll(ravi, cloud);
        service.enroll(neha, cloud);
        service.enroll(kiran, cloud);
        service.drop(asha, cloud);
    }
}
