import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class LibraryMember {
    private String membershipId;
    private String name;
    private boolean premiumMember;
    private String securityAnswerDigest;

    LibraryMember() {
        this(null);
    }

    LibraryMember(String name) {
        this(null, name);
    }

    LibraryMember(String membershipId, String name) {
        this.membershipId = membershipId;
        this.name = name;
    }

    String getMembershipId() {
        return membershipId;
    }

    void setMembershipId(String id) {
        if (this.membershipId == null && id != null) {
            this.membershipId = id;
        }
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    boolean isPremiumMember() {
        return premiumMember;
    }

    void setPremiumMember(boolean premium) {
        this.premiumMember = premium;
    }

    void setSecurityAnswer(String answer) {
        this.securityAnswerDigest = digest(answer);
    }

    private static String digest(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(new LibraryMember("Priya Nair").getMembershipId());
        System.out.println(new LibraryMember("LIB-8841", "Priya Nair").getMembershipId());
        LibraryMember m = new LibraryMember();
        m.setMembershipId("LIB-8841");
        m.setMembershipId("FAKE-0000");
        System.out.println(m.getMembershipId());
        m.setSecurityAnswer("mother's maiden name");
    }
}