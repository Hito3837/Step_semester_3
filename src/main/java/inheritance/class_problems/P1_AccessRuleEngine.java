class AccessRuleEngine {
    /* A linter that warns engineers about illegal field access BEFORE they even try to
       compile. The decision is made purely from Java's visibility rules — no field names
       are hardcoded anywhere: the same rule applies to any member carrying a modifier. */
    static String classifyAccess(String fieldModifier, String accessorContext) {
        switch (fieldModifier) {
            case "public":
                /* public is visible everywhere, period. */
                return "ALLOWED";
            case "private":
                /* private is visible only inside the owning class itself. */
                return "SAME_CLASS".equals(accessorContext) ? "ALLOWED" : "DENIED";
            case "default":
                /* Package-private (no modifier) is visible inside the class and anywhere in
                   the same package, but never across package boundaries. */
                return "SAME_CLASS".equals(accessorContext) || "SAME_PACKAGE".equals(accessorContext)
                        ? "ALLOWED" : "DENIED";
            case "protected":
                /* In these three basic contexts protected behaves exactly like default:
                   the constant-only difference appears once inheritance enters (Problem 2). */
                return "SAME_CLASS".equals(accessorContext) || "SAME_PACKAGE".equals(accessorContext)
                        ? "ALLOWED" : "DENIED";
            default:
                return "DENIED";
        }
    }

    static String summarizeBatch(String[][] attempts) {
        int allowed = 0;
        int denied = 0;
        for (String[] row : attempts) {
            if ("ALLOWED".equals(classifyAccess(row[0], row[1]))) {
                allowed++;
            } else {
                denied++;
            }
        }
        return "Allowed: " + allowed + " | Denied: " + denied;
    }

    public static void main(String[] args) {
        System.out.println(classifyAccess("private", "SAME_CLASS"));
        System.out.println(classifyAccess("default", "DIFFERENT_PACKAGE"));
        System.out.println(summarizeBatch(new String[][]{
                {"protected", "SAME_PACKAGE"},
                {"protected", "DIFFERENT_PACKAGE"},
                {"public", "DIFFERENT_PACKAGE"}
        }));
        try {
            new PatientRecord("MT9", "W3", 98.2, "MediTrack Central");
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected");
        }
        PatientRecord ok = new PatientRecord("MT94", "W3", 98.2, "MediTrack Central");
        System.out.println(ok.getPatientId() + " in ward " + ok.getWardCode()
                + ", vitals " + ok.getVitalsScore() + ", facility " + ok.facilityName);
    }
}

/* The class those rules protect. patientId/wardCode/vitalsScore are private (a valid record
   must not let its critical fields be scribbled over from outside); facilityName is public
   final because a facility name is shared, non-sensitive routing data that never changes.
   There is no usable no-argument constructor: an invalid record can never be built. */
class PatientRecord {
    private final String patientId;
    private final String wardCode;
    private final double vitalsScore;
    public final String facilityName;

    public PatientRecord(String patientId, String wardCode, double vitalsScore, String facilityName) {
        if (!isValidId(patientId)) {
            throw new IllegalArgumentException("patientId must be non-blank, at least 4 characters after trimming");
        }
        this.patientId = patientId.trim();
        this.wardCode = wardCode;
        this.vitalsScore = vitalsScore;
        this.facilityName = facilityName;
    }

    /* Trim first, then check blankness and minimum length in a single pass: a value that's
       all spaces fails the same way an empty string does. */
    private static boolean isValidId(String id) {
        if (id == null) {
            return false;
        }
        String trimmed = id.trim();
        return !trimmed.isEmpty() && trimmed.length() >= 4;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getWardCode() {
        return wardCode;
    }

    public double getVitalsScore() {
        return vitalsScore;
    }
}