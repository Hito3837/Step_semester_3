class SrmStudent {
    private final String name;
    private final String regNo;
    private int attendance;

    public SrmStudent(String name, String regNo, int attendance) {
        if (name == null || name.trim().isEmpty() || regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException("name and regNo cannot be blank");
        }
        if (attendance < 0 || attendance > 100) {
            throw new IllegalArgumentException("attendance must be between 0 and 100");
        }
        this.name = name.trim();
        this.regNo = regNo.trim();
        this.attendance = attendance;
    }

    void addAttendanceUpdate(int newAttendance) {
        if (newAttendance < 0 || newAttendance > 100) {
            throw new IllegalArgumentException("attendance must be between 0 and 100");
        }
        this.attendance = newAttendance;
    }

    boolean isEligible() {
        return attendance >= 75;
    }

    String getName() {
        return name;
    }

    int getAttendance() {
        return attendance;
    }

    /* classAverage() is static because it is a class-level utility: it reads the
       attendance of a whole group passed in as an argument and has no "own" student
       to act on. isEligible() is an instance method because it answers a question
       about one specific student's own attendance value. */
    static double classAverage(SrmStudent[] students) {
        int total = 0;
        for (SrmStudent s : students) {
            total += s.attendance;
        }
        return (double) total / students.length;
    }

    public static void main(String[] args) {
        SrmStudent[] classRoom = {
                new SrmStudent("Ravi", "RA231100301011", 82),
                new SrmStudent("Anitha", "RA231100301012", 68),
                new SrmStudent("Karthik", "RA231100301013", 91),
                new SrmStudent("Meera", "RA231100301014", 74),
                new SrmStudent("Suresh", "RA231100301015", 60)
        };
        for (SrmStudent s : classRoom) {
            System.out.println(s.getName() + " - " + s.getAttendance() + "% - "
                    + (s.isEligible() ? "Eligible" : "Detained"));
        }
        System.out.println("Class average: " + SrmStudent.classAverage(classRoom) + "%");
    }
}