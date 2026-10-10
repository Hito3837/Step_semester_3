// One-Click Data Export — unrelated classes sharing one export action via an interface.
// ReportGenerator and UserProfile share no parent and no relation: the interface gives them
// one shared 'export' action without forcing any hierarchy. Their only common home is the
// single shared export counter in ExportTracker, incremented on every exportData() call.

interface Exportable {
    String exportData();
}

class ExportTracker {
    private static int totalExports;

    static void record() {
        totalExports++;
    }

    static int total() {
        return totalExports;
    }
}

class ReportGenerator implements Exportable {
    private final String reportName;

    public ReportGenerator(String reportName) {
        this.reportName = reportName;
    }

    @Override
    public String exportData() {
        ExportTracker.record();
        return "Exported report: " + reportName;
    }
}

class UserProfile implements Exportable {
    private final String username;

    public UserProfile(String username) {
        this.username = username;
    }

    @Override
    public String exportData() {
        ExportTracker.record();
        return "Exported profile: " + username;
    }
}

public class DataExport {
    static int getTotalExports() {
        return ExportTracker.total();
    }

    static void exportAll(Exportable[] items) {
        for (Exportable item : items) {
            System.out.println(item.exportData());
        }
    }

    public static void main(String[] args) {
        ReportGenerator r = new ReportGenerator("Sales Q1");
        UserProfile u = new UserProfile("jane_doe");

        // Each PDF example assumes an isolated run, so the shared-counter example runs first
        // on this JVM: exactly two exportData() calls happen below, hence the total of 2.
        Exportable ref = r; // upcasting: ReportGenerator stored as the Exportable interface type
        exportAll(new Exportable[] { ref, u });
        System.out.println(getTotalExports());

        System.out.println(r.exportData());
        System.out.println(u.exportData());
    }
}
