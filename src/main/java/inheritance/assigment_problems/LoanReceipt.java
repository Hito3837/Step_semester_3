class LoanReceipt {
    private final String memberId;
    private final String[] bookIds;

    LoanReceipt(String memberId, String[] bookIds) {
        if (bookIds == null) {
            throw new IllegalArgumentException("bookIds cannot be null");
        }
        for (String id : bookIds) {
            if (id == null || !id.matches("BK-\\d{3}")) {
                throw new IllegalArgumentException("invalid book id: " + id);
            }
        }
        this.memberId = memberId;
        this.bookIds = bookIds.clone();
    }

    String getMemberId() {
        return memberId;
    }

    String[] getBookIds() {
        return bookIds.clone();
    }

    LoanReceipt withCorrectedBookId(int index, String newId) {
        if (newId == null || !newId.matches("BK-\\d{3}")) {
            throw new IllegalArgumentException("invalid book id: " + newId);
        }
        String[] corrected = getBookIds();
        corrected[index] = newId;
        return new LoanReceipt(memberId, corrected);
    }
}

class ReferenceOnlyLoanReceipt extends LoanReceipt {
    private final String roomNumber;

    ReferenceOnlyLoanReceipt(String memberId, String[] bookIds, String roomNumber) {
        super(memberId, bookIds);
        this.roomNumber = roomNumber;
    }

    String getRoomNumber() {
        return roomNumber;
    }
}