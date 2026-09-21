class SrmStudentBroken {
    /* Reproducing the bug: every field is static, so one copy is shared by the whole class. */
    static String name;
    static String regNo;
    static int attendance;

    SrmStudentBroken(String name, String regNo, int attendance) {
        SrmStudentBroken.name = name;
        SrmStudentBroken.regNo = regNo;
        SrmStudentBroken.attendance = attendance;
    }
}

class SrmStudent {
    /* Instance fields: each student owns their own copy. Marking these static would be
       wrong because:
       - name: a name belongs to one student; a single shared static String means every
         new student silently overwrites the previous student's name.
       - regNo: a registration number must be unique per student; static would force a
         single shared value that all students read back.
       - attendance: attendance is per-student data; one shared static int would make
         every student report the attendance of the last student constructed. */
    private final String name;
    private final String regNo;
    private final int attendance;

    /* True class-level state: genuinely shared across all students. */
    static final String university = "SRM Institute of Science and Technology";
    static int admissionCount = 0;

    SrmStudent(String name, int attendance) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
        if (attendance < 0 || attendance > 100) {
            throw new IllegalArgumentException("attendance must be between 0 and 100");
        }
        admissionCount++;
        this.name = name.trim();
        this.regNo = "RA2311003010" + (10 + admissionCount);
        this.attendance = attendance;
    }

    void printIdCard() {
        System.out.println(name + " | " + regNo);
    }

    static void printTotalAdmissions() {
        System.out.println("Students admitted so far: " + admissionCount);
    }

    public static void main(String[] args) {
        System.out.println("Broken version - static fields shared by every student:");
        SrmStudentBroken b1 = new SrmStudentBroken("Ravi", "FAKE-1", 82);
        SrmStudentBroken b2 = new SrmStudentBroken("Meera", "FAKE-2", 91);
        System.out.println(b1.name);
        System.out.println(b2.name);
        System.out.println("(Ravi's data was overwritten - both students now show \"Meera\")");

        System.out.println("Fixed version - independent instance fields:");
        SrmStudent s1 = new SrmStudent("Ravi", 82);
        SrmStudent s2 = new SrmStudent("Meera", 91);
        s1.printIdCard();
        s2.printIdCard();
        SrmStudent.printTotalAdmissions();
    }
}