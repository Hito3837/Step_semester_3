class BookInventory {
    private final int copiesTotal;
    private int copiesAvailable;

    BookInventory(int copiesTotal) {
        if (copiesTotal <= 0) {
            throw new IllegalArgumentException("copiesTotal must be positive");
        }
        this.copiesTotal = copiesTotal;
        this.copiesAvailable = copiesTotal;
    }

    void checkOut() {
        if (copiesAvailable > 0) {
            copiesAvailable--;
        }
    }

    void checkIn() {
        if (copiesAvailable < copiesTotal) {
            copiesAvailable++;
        }
    }

    int getCopiesAvailable() {
        return copiesAvailable;
    }

    public static void main(String[] args) {
        try {
            new BookInventory(0);
            System.out.println("constructed");
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected");
        }
        BookInventory b = new BookInventory(3);
        b.checkOut();
        b.checkOut();
        b.checkOut();
        b.checkOut();
        System.out.println("after 4 checkouts: " + b.getCopiesAvailable());
        b.checkIn();
        b.checkIn();
        b.checkIn();
        b.checkIn();
        System.out.println("after 4 check-ins: " + b.getCopiesAvailable());
    }
}