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

    String getRegNo() {
        return regNo;
    }
}

/* Both subclasses extend the tested FeeAccount without editing it. */
class HostelFeeAccount extends FeeAccount {
    HostelFeeAccount(String regNo, double totalFee) {
        super(regNo, totalFee);
    }

    /* amount is split across two installments: only the first half is due right now,
       the second half falls due later, so the outstanding due drops by amount / 2. */
    void payInTwoInstallments(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("installment amount must be positive");
        }
        pay(amount / 2.0);
    }
}

class ScholarshipFeeAccount extends FeeAccount {
    private final double scholarshipPercent;

    ScholarshipFeeAccount(String regNo, double totalFee, double scholarshipPercent) {
        super(regNo, totalFee);
        if (scholarshipPercent < 0 || scholarshipPercent > 100) {
            throw new IllegalArgumentException("scholarshipPercent must be between 0 and 100");
        }
        this.scholarshipPercent = scholarshipPercent;
    }

    double effectiveDue() {
        return getDue() * (1.0 - scholarshipPercent / 100.0);
    }
}

class FeeAccountMain {
    public static void main(String[] args) {
        FeeAccount plain = new FeeAccount("RA231100301011", 150000);
        plain.pay(150000);

        HostelFeeAccount hostel = new HostelFeeAccount("RA231100301012", 200000);
        hostel.payInTwoInstallments(120000);

        ScholarshipFeeAccount scholar = new ScholarshipFeeAccount("RA231100301013", 180000, 20);

        FeeAccount[] accounts = {plain, hostel, scholar};
        for (FeeAccount a : accounts) {
            if (a instanceof ScholarshipFeeAccount) {
                System.out.println("Scholarship account effective due: Rs "
                        + ((ScholarshipFeeAccount) a).effectiveDue());
            } else if (a instanceof HostelFeeAccount) {
                System.out.println("Hostel account due: Rs " + a.getDue());
            } else {
                System.out.println("Plain account due: Rs " + a.getDue());
            }
        }
    }
}