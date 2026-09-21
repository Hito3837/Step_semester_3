
public class ParkingSlot
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
    public static void safeAllot(ParkingSlot[] slots, String vehicleNo)
    {
        ParkingSlot available = findAvailableSlot(slots);
        if (available != null)
        {
            available.allot(vehicleNo);
            System.out.println(vehicleNo + " allotted to slot " + available.getSlotNo());
        }
        else
        {
            System.out.println("No slots available for " + vehicleNo);
        }
    }
    public static void main(String[] args)
    {
        java.util.Scanner sc = new java.util.Scanner(System.in);
        ParkingSlot[] slots = new ParkingSlot[2];
        System.out.print("Enter slot 1 number: ");
        slots[0] = new ParkingSlot(sc.nextLine(), 4, 3);
        System.out.print("Enter slot 2 number: ");
        slots[1] = new ParkingSlot(sc.nextLine(), 5, 5);
        System.out.print("Enter vehicle number: ");
        String vehicleNo = sc.nextLine();
        safeAllot(slots, vehicleNo);
        sc.close();
    }
}
