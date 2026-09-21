class FeeAccount {
    private final String regNo;
    private final double totalFee;
    private double amountPaid;

    public FeeAccount(String regNo, double totalFee) {
        if (regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException("regNo cannot be blank");
        }
        if (totalFee <= 0) {
            throw new IllegalArgumentException("totalFee must be positive");
        }
        this.regNo = regNo.trim();
        this.totalFee = totalFee;
        this.amountPaid = 0.0;
    }

    void pay(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("payment amount must be positive");
        }
        amountPaid += amount;
    }

    double getDue() {
        return totalFee - amountPaid;
    }
}

class HostelFeeAccount extends FeeAccount {
    HostelFeeAccount(String regNo, double totalFee) {
        super(regNo, totalFee);
    }

    void payInTwoInstallments(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("installment amount must be positive");
        }
        pay(amount / 2.0);
    }
}

class HostelRoom {
    private final String roomNo;
    private final int beds;
    private int occupied;

    public HostelRoom(String roomNo, int beds) {
        if (roomNo == null || roomNo.trim().isEmpty()) {
            throw new IllegalArgumentException("roomNo cannot be blank");
        }
        if (beds <= 0) {
            throw new IllegalArgumentException("beds must be positive");
        }
        this.roomNo = roomNo.trim();
        this.beds = beds;
        this.occupied = 0;
    }

    boolean allot(String name) {
        if (occupied < beds) {
            occupied++;
            return true;
        }
        return false;
    }

    String getRoomNo() {
        return roomNo;
    }

    int getOccupied() {
        return occupied;
    }

    int getBeds() {
        return beds;
    }

    static HostelRoom findAvailableRoom(HostelRoom[] rooms) {
        for (HostelRoom room : rooms) {
            if (room.occupied < room.beds) {
                return room;
            }
        }
        return null;
    }
}

class SrmStudent {
    String name;
    String regNo;
    HostelFeeAccount feeAccount;
    HostelRoom room;

    static int totalStudents = 0;

    SrmStudent(String name, String regNo, HostelFeeAccount feeAccount) {
        this.name = name;
        this.regNo = regNo;
        this.feeAccount = feeAccount;
        totalStudents++;
    }

    String fullStatus() {
        return name + " | Due: Rs " + feeAccount.getDue() + " | Room: "
                + (room == null ? "unallotted" : room.getRoomNo());
    }

    public static void main(String[] args) {
        HostelFeeAccount raviFee = new HostelFeeAccount("RA231100301011", 200000);
        raviFee.pay(60000);
        SrmStudent ravi = new SrmStudent("Ravi", "RA231100301011", raviFee);

        HostelFeeAccount anithaFee = new HostelFeeAccount("RA231100301012", 200000);
        anithaFee.pay(20000);
        SrmStudent anitha = new SrmStudent("Anitha", "RA231100301012", anithaFee);

        HostelFeeAccount karthikFee = new HostelFeeAccount("RA231100301013", 200000);
        SrmStudent karthik = new SrmStudent("Karthik", "RA231100301013", karthikFee);

        HostelRoom[] rooms = {new HostelRoom("C-214", 3), new HostelRoom("C-507", 2)};
        HostelRoom first = HostelRoom.findAvailableRoom(rooms);
        first.allot("Ravi");
        ravi.room = first;
        HostelRoom second = null;
        for (HostelRoom room : rooms) {
            if (room != first && room.getOccupied() < room.getBeds()) {
                second = room;
                break;
            }
        }
        second.allot("Anitha");
        anitha.room = second;

        try {
            karthik.feeAccount.pay(-100);
        } catch (IllegalArgumentException e) {
            System.out.println("Payment rejected: " + e.getMessage());
        }

        System.out.println(ravi.fullStatus());
        System.out.println(anitha.fullStatus());
        System.out.println(karthik.fullStatus());
        System.out.println("Total students: " + SrmStudent.totalStudents);
    }
}