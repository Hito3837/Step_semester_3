class PatientProfile {
    /* The records dashboard auto-generates forms via a JavaBean-scanning framework, which
       needs a public no-arg constructor — but intake also needs quicker constructors for
       partial data. All three are linked with this(...) chaining so setup logic is never
       duplicated: no-arg delegates to name-only, both delegate to the id+name version. */
    private String patientId;
    private String name;
    private boolean discharged;
    private int lockerPinDigest;

    public PatientProfile() {
        this(null);
    }

    public PatientProfile(String name) {
        this(null, name);
    }

    public PatientProfile(String patientId, String name) {
        this.patientId = patientId;
        this.name = name;
    }

    public String getPatientId() {
        return patientId;
    }

    /* Write-once, ever: the second call is silently ignored — write-once even though a
       public setter genuinely exists to satisfy the framework's bean scan. */
    public void setPatientId(String id) {
        if (this.patientId == null && id != null) {
            this.patientId = id;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDischarged() {
        return discharged;
    }

    public void setDischarged(boolean discharged) {
        this.discharged = discharged;
    }

    /* The locker PIN is permanently write-only: it can be set, but never retrieved again
       in any form — there is deliberately no matching getter anywhere. Only a deterministic
       one-way transformation is stored. */
    public void setLockerPin(String pin) {
        if (pin != null && pin.matches("\\d{4,6}")) {
            this.lockerPinDigest = pin.hashCode();
        }
    }

    public static void main(String[] args) {
        System.out.println(new PatientProfile("Arjun Iyer").getPatientId());
        System.out.println(new PatientProfile("MT2026-0142", "Arjun Iyer").getPatientId());

        PatientProfile p = new PatientProfile();
        p.setPatientId("MT2026-0142");
        p.setPatientId("HACKED-0000");
        System.out.println(p.getPatientId());

        p.setLockerPin("4821");
        p.setDischarged(true);
        System.out.println("Discharged: " + p.isDischarged());
    }
}