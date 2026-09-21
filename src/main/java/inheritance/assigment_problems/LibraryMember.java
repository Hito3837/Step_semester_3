class LibraryMember {
    private final String membershipId;
    private String branchCode;
    protected double finesOwed;
    public String displayName;

    LibraryMember(String membershipId, String branchCode, double finesOwed, String displayName) {
        String trimmed = membershipId == null ? null : membershipId.trim();
        if (trimmed == null || trimmed.isEmpty() || trimmed.length() < 4) {
            throw new IllegalArgumentException("membershipId must be at least 4 characters");
        }
        this.membershipId = trimmed;
        this.branchCode = branchCode;
        this.finesOwed = finesOwed;
        this.displayName = displayName;
    }

    public static void main(String[] args) {
        try {
            new LibraryMember("LB9", "BR1", 0, "Priya Nair");
            System.out.println("constructed");
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected");
        }
        LibraryMember ok = new LibraryMember("LB94", "BR1", 0, "Priya Nair");
        System.out.println("membershipId = " + ok.membershipId);
    }
}