import java.util.Scanner;
class Employee
{
    private int empId;
    private String empName;
    private double salary;
    public Employee(int empId, String empName, double salary)
    {
        this.empId = empId;
        this.empName = empName;
        this.salary = salary;
    }
    public double getSalary()
    {
        return salary;
    }
    public int getEmpId()
    {
        return empId;
    }
    public String getEmpName()
    {
        return empName;
    }
}
class ManagerEmployee extends Employee
{
    private double teamBonus;
    public ManagerEmployee(int empId, String empName, double salary, double teamBonus)
    {
        super(empId, empName, salary);
        this.teamBonus = teamBonus;
    }
    public double effectiveSalary()
    {
        return getSalary() + teamBonus;
    }
}
class ParkingSlot
{
    private String slotNo;
    private int capacity;
    private int occupiedCount;
    public ParkingSlot(String slotNo, int capacity, int occupiedCount)
    {
        this.slotNo = slotNo;
        this.capacity = capacity;
        this.occupiedCount = occupiedCount;
    }
    public void allot(String vehicleNo)
    {
        occupiedCount++;
    }
    public String getSlotNo()
    {
        return slotNo;
    }
    public int getCapacity()
    {
        return capacity;
    }
    public int getOccupiedCount()
    {
        return occupiedCount;
    }
    public static ParkingSlot findAvailableSlot(ParkingSlot[] slots)
    {
        for (ParkingSlot slot : slots)
        {
            if (slot.occupiedCount < slot.capacity)
            {
                return slot;
            }
        }
        return null;
    }
    public static ParkingSlot allocateSlot(ParkingSlot[] slots, String vehicleNo)
    {
        ParkingSlot available = findAvailableSlot(slots);
        if (available != null)
        {
            available.allot(vehicleNo);
            return available;
        }
        return null;
    }
}
public class CompanyEmployeeRecord
{
    private String name;
    private String empId;
    private Employee employee;
    private ParkingSlot slot;
    private static int totalRecords = 0;
    public CompanyEmployeeRecord(String name, String empId, Employee employee)
    {
        this.name = name;
        this.empId = empId;
        this.employee = employee;
        this.slot = null;
        totalRecords++;
    }
    public void assignSlot(ParkingSlot slot)
    {
        this.slot = slot;
    }
    public String fullProfile()
    {
        double pay = 0;
        if (employee instanceof ManagerEmployee)
        {
            pay = ((ManagerEmployee) employee).effectiveSalary();
        }
        else
        {
            pay = employee.getSalary();
        }
        String slotInfo = (slot != null) ? slot.getSlotNo() : "no parking assigned"; return name + " | Pay: Rs " + pay + " | Slot: " + slotInfo;
    }
    public static int getTotalRecords()
    {
        return totalRecords;
    }
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        ParkingSlot[] slots = { new ParkingSlot("A1", 1, 0), new ParkingSlot("A2", 1, 0)};
        System.out.print("Enter employee 1 base salary: ");
        double sal1 = sc.nextDouble();
        System.out.print("Enter employee 1 team bonus: ");
        double bonus1 = sc.nextDouble();
        ManagerEmployee emp1 = new ManagerEmployee(1, "Divya", sal1, bonus1);
        System.out.print("Enter employee 2 base salary: ");
        double sal2 = sc.nextDouble();
        System.out.print("Enter employee 2 team bonus: ");
        double bonus2 = sc.nextDouble();
        ManagerEmployee emp2 = new ManagerEmployee(2, "Karan", sal2, bonus2);
        System.out.print("Enter employee 3 salary: ");
        double sal3 = sc.nextDouble();
        Employee emp3 = new Employee(3, "Meera", sal3);
        sc.nextLine();
        System.out.print("Enter vehicle 1 number: ");
        String car1 = sc.nextLine();
        System.out.print("Enter vehicle 2 number: ");
        String car2 = sc.nextLine();
        CompanyEmployeeRecord record1 = new CompanyEmployeeRecord("Divya", "EMP001", emp1);
        CompanyEmployeeRecord record2 = new CompanyEmployeeRecord("Karan", "EMP002", emp2);
        CompanyEmployeeRecord record3 = new CompanyEmployeeRecord("Meera", "EMP003", emp3);
        ParkingSlot slot1 = ParkingSlot.allocateSlot(slots, car1);
        if (slot1 != null)
        {
            record1.assignSlot(slot1);
        }
        ParkingSlot slot2 = ParkingSlot.allocateSlot(slots, car2);
        if (slot2 != null)
        {
            record2.assignSlot(slot2);
        }
        System.out.println(record1.fullProfile());
        System.out.println(record2.fullProfile());
        System.out.println(record3.fullProfile());
        System.out.println("Total records: " + CompanyEmployeeRecord.getTotalRecords());
        sc.close();
    }
}
