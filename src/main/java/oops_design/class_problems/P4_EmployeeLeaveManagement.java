import java.time.LocalDate;
import java.util.*;

// Employee Leave Request Management.
// Classes / relationships / multiplicities:
//   Employee (1) ---- (*) LeaveRequest : an employee may file many requests, each request belongs to one employee
//   Employee <|-- FullTimeEmployee, PartTimeEmployee, ContractEmployee (inheritance; each type carries
//                its own leave policy via maxPaidDays(), new types plug in without touching the workflow)
//   LeaveManager handles submission + review (approval service).
// Status state machine: PENDING -> APPROVED | PENDING -> REJECTED (one-way; APPROVED/REJECTED can never
// revert to PENDING — enforced inside LeaveRequest.setStatus, so no caller can break the invariant).

enum LeaveStatus {
    PENDING, APPROVED, REJECTED
}

abstract class Employee {
    private final String name;

    Employee(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    abstract String getType();

    abstract int maxPaidDays();
}

class FullTimeEmployee extends Employee {
    FullTimeEmployee(String name) {
        super(name);
    }

    @Override
    String getType() {
        return "Full-time";
    }

    @Override
    int maxPaidDays() {
        return 24;
    }
}

class PartTimeEmployee extends Employee {
    PartTimeEmployee(String name) {
        super(name);
    }

    @Override
    String getType() {
        return "Part-time";
    }

    @Override
    int maxPaidDays() {
        return 12;
    }
}

class ContractEmployee extends Employee {
    ContractEmployee(String name) {
        super(name);
    }

    @Override
    String getType() {
        return "Contract";
    }

    @Override
    int maxPaidDays() {
        return 6;
    }
}

class LeaveRequest {
    private final Employee employee;
    private final LocalDate from;
    private final LocalDate to;
    private LeaveStatus status = LeaveStatus.PENDING;

    LeaveRequest(Employee employee, LocalDate from, LocalDate to) {
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("Leave end date cannot be before start date.");
        }
        this.employee = employee;
        this.from = from;
        this.to = to;
    }

    Employee getEmployee() {
        return employee;
    }

    LeaveStatus getStatus() {
        return status;
    }

    // Central state-transition guard: only PENDING requests may move to APPROVED/REJECTED.
    // Returns true when the transition was applied, false when it was rejected.
    boolean setStatus(LeaveStatus next) {
        if (status != LeaveStatus.PENDING) {
            System.out.println("Cannot change status: " + label(status) + " request cannot revert to "
                    + label(next) + ".");
            return false;
        }
        if (next == LeaveStatus.PENDING) {
            return false;
        }
        status = next;
        return true;
    }

    static String label(LeaveStatus value) {
        String name = value.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

    @Override
    public String toString() {
        return "Leave request submitted by " + employee.getName() + " for " + from + " to " + to
                + ". Status: " + label(status) + ".";
    }
}

class LeaveManager {
    private final List<LeaveRequest> requests = new ArrayList<>();

    LeaveRequest submit(Employee employee, LocalDate from, LocalDate to) {
        LeaveRequest request = new LeaveRequest(employee, from, to);
        requests.add(request);
        System.out.println(request);
        return request;
    }

    void approve(LeaveRequest request) {
        if (request.setStatus(LeaveStatus.APPROVED)) {
            System.out.println("Leave request for " + request.getEmployee().getName()
                    + " approved. Status: " + LeaveRequest.label(request.getStatus()) + ".");
        }
    }

    void reject(LeaveRequest request) {
        if (request.setStatus(LeaveStatus.REJECTED)) {
            System.out.println("Leave request for " + request.getEmployee().getName()
                    + " rejected. Status: " + LeaveRequest.label(request.getStatus()) + ".");
        }
    }
}

public class P4_EmployeeLeaveManagement {
    public static void main(String[] args) {
        LeaveManager manager = new LeaveManager();
        Employee john = new FullTimeEmployee("John Doe");
        Employee jane = new PartTimeEmployee("Jane Smith");

        LeaveRequest johns = manager.submit(john, LocalDate.of(2024, 10, 10), LocalDate.of(2024, 10, 12));
        manager.approve(johns);
        manager.submit(jane, LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 5));
        johns.setStatus(LeaveStatus.PENDING); // illegal revert -> must be refused
    }
}
