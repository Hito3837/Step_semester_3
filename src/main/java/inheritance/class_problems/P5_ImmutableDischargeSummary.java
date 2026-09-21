class DischargeSummary {
    /* A permanent legal document that must never change again. Every field is final, every
       medication code is validated against the shared format rule — a bad code rejects the
       whole construction rather than being silently dropped. Immutability is enforced
       structurally (final fields, defensive copies both directions, no setters, the wither
       pattern) rather than with the final keyword on the class itself, because the required
       CriticalCareDischargeSummary must subclass it; the subclass is the final leaf type. */
    private final String patientId;
    private final String[] medicationCodes;

    /* One-time shared state set up exactly once when the class is first loaded. */
    private static final String MEDICATION_CODE_PATTERN;

    static {
        MEDICATION_CODE_PATTERN = "MED-[A-Z]";
    }

    public DischargeSummary(String patientId, String[] medicationCodes) {
        if (patientId == null || patientId.trim().isEmpty()) {
            throw new IllegalArgumentException("patientId cannot be blank");
        }
        if (medicationCodes == null) {
            throw new IllegalArgumentException("medicationCodes cannot be null");
        }
        for (String code : medicationCodes) {
            if (!isValidMedicationCode(code)) {
                throw new IllegalArgumentException("invalid medication code: " + code);
            }
        }
        this.patientId = patientId.trim();
        /* Defensive copy on the way in: "MED-" + exactly one uppercase letter. */
        this.medicationCodes = medicationCodes.clone();
    }

    private static boolean isValidMedicationCode(String code) {
        return code != null && code.matches(MEDICATION_CODE_PATTERN);
    }

    public String getPatientId() {
        return patientId;
    }

    /* Defensive copy on the way out, every time — tampering with the returned array must
       not touch the summary. */
    public String[] getMedicationCodes() {
        return medicationCodes.clone();
    }

    /* "Changing" a summary means building a brand-new immutable object (wither pattern):
       a corrected copy, never a mutation of the original. */
    public DischargeSummary withCorrectedMedication(int index, String newCode) {
        if (index < 0 || index >= medicationCodes.length) {
            throw new IllegalArgumentException("index out of range");
        }
        if (!isValidMedicationCode(newCode)) {
            throw new IllegalArgumentException("invalid medication code: " + newCode);
        }
        String[] corrected = medicationCodes.clone();
        corrected[index] = newCode;
        return new DischargeSummary(patientId, corrected);
    }
}

/* Critical-care variant: everything the routine summary provides, plus ICU duration. It is
   genuinely a DischargeSummary at runtime, which is exactly what instanceof dispatches on.
   final on the leaf type so no further subclassing of a legal document is allowed. */
final class CriticalCareDischargeSummary extends DischargeSummary {
    private final int icuDays;

    public CriticalCareDischargeSummary(String patientId, String[] medicationCodes, int icuDays) {
        super(patientId, medicationCodes);
        if (icuDays < 0) {
            throw new IllegalArgumentException("icuDays cannot be negative");
        }
        this.icuDays = icuDays;
    }

    public int getIcuDays() {
        return icuDays;
    }
}

class DischargeLedger {
    /* Shared processor counters, reset fresh for every batch run. */
    static int processed;
    static int nullSkipped;
    static int criticalCount;
    static int routineCount;

    static {
        processed = 0;
        nullSkipped = 0;
        criticalCount = 0;
        routineCount = 0;
    }

    /* Settles a critical-care summary differently from a routine one via instanceof, and
       never throws on a null batch entry. */
    static String processNightlyBatch(DischargeSummary[] summaries) {
        processed = 0;
        nullSkipped = 0;
        criticalCount = 0;
        routineCount = 0;
        if (summaries == null) {
            return "0 processed | 0 null skipped | 0 critical-care | 0 routine";
        }
        for (DischargeSummary summary : summaries) {
            if (summary == null) {
                nullSkipped++;
                continue;
            }
            if (summary instanceof CriticalCareDischargeSummary) {
                criticalCount++;
            } else {
                routineCount++;
            }
            processed++;
        }
        return processed + " processed | " + nullSkipped + " null skipped | "
                + criticalCount + " critical-care | " + routineCount + " routine";
    }

    public static void main(String[] args) {
        try {
            new DischargeSummary("MT2026-0142", new String[]{"MED-A", "bad"});
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected");
        }

        DischargeSummary d = new DischargeSummary("MT2026-0142", new String[]{"MED-A", "MED-B"});
        String[] codes = d.getMedicationCodes();
        codes[0] = "TAMPERED";
        System.out.println(d.getMedicationCodes()[0]);

        DischargeSummary fixed = d.withCorrectedMedication(1, "MED-C");
        System.out.println(java.util.Arrays.toString(fixed.getMedicationCodes()));

        System.out.println(processNightlyBatch(new DischargeSummary[]{
                new CriticalCareDischargeSummary("MT001", new String[]{"MED-X"}, 4),
                null,
                new DischargeSummary("MT002", new String[]{"MED-Y"})
        }));
    }
}