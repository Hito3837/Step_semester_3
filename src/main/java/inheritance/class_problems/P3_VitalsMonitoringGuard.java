import java.util.ArrayList;
import java.util.List;

class PatientVitals {
    /* A previous intern left vitalsScore as a public field and a physically impossible
       reading (-2C) slipped straight into a patient's chart. Now every field is private
       and the only way in or out is through validated methods, including a constructor
       that reuses recordReading(...) itself — so the range rule lives in exactly one place. */
    private final List<Double> readings = new ArrayList<>();

    public PatientVitals(double[] initialReadings) {
        if (initialReadings != null) {
            for (double reading : initialReadings) {
                recordReading(reading);
            }
        }
    }

    /* Silently reject readings at or below 0C and above 45C — no exception, just a clean
       rejection. The invalid seed value in the constructor is filtered the exact same way
       it would be if recordReading(-2) had been called directly afterward. */
    void recordReading(double reading) {
        if (reading <= 0.0 || reading > 45.0) {
            return;
        }
        readings.add(reading);
    }

    double getAverage() {
        if (readings.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (double reading : readings) {
            sum += reading;
        }
        return sum / readings.size();
    }

    /* A fresh defensive copy every single call: mutating what a caller receives must never
       reach back into the stored history. */
    double[] getAllReadings() {
        double[] copy = new double[readings.size()];
        for (int i = 0; i < copy.length; i++) {
            copy[i] = readings.get(i);
        }
        return copy;
    }

    public static void main(String[] args) {
        PatientVitals v = new PatientVitals(new double[]{36.5, -2, 37.1});
        System.out.println(java.util.Arrays.toString(v.getAllReadings()));

        double[] copy = v.getAllReadings();
        copy[0] = 999;
        System.out.println(v.getAllReadings()[0]);

        v.recordReading(46.0);
        v.recordReading(36.8);
        System.out.println(java.util.Arrays.toString(v.getAllReadings()));
        System.out.println("Average: " + v.getAverage());
    }
}