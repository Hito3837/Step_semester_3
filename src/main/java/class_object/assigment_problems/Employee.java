import java.util.Scanner;
public class Employee
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
class InternEmployee extends Employee
{
    private double stipendCap;
    public InternEmployee(int empId, String empName, double salary, double stipendCap)
    {
        super(empId, empName, salary);
        this.stipendCap = stipendCap;
    }
    public double effectiveSalary()
    {
        return Math.min(getSalary(), stipendCap);
    }
}
class EmployeeMain
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter plain employee salary: ");
        double plainSalary = sc.nextDouble();
        Employee plainEmp = new Employee(1, "John", plainSalary);
        System.out.print("Enter manager salary: ");
        double managerSalary = sc.nextDouble();
        System.out.print("Enter manager team bonus: ");
        double teamBonus = sc.nextDouble();
        ManagerEmployee manager = new ManagerEmployee(2, "Alice", managerSalary, teamBonus);
        System.out.print("Enter intern salary: ");
        double internSalary = sc.nextDouble();
        System.out.print("Enter intern stipend cap: ");
        double stipendCap = sc.nextDouble();
        InternEmployee intern = new InternEmployee(3, "Bob", internSalary, stipendCap);
        System.out.println("Plain employee pay: Rs " + plainEmp.getSalary());
        System.out.println("Manager effective pay: Rs " + manager.effectiveSalary());
        System.out.println("Intern effective pay: Rs " + intern.effectiveSalary());
        sc.close();
    }
}
