// Home Safety Alert Network — capability interface, multilevel hierarchy, safe downcast.
// Alertable is a pure capability contract: SmokeDetector implements it with zero connection
// to SecuritySensor, proving shared ancestry is never required — only a shared promise.
// DualZoneMotionSensor reuses MotionSensor's message via super.sendAlert(message) and only
// appends its second zone, the same super-reuse pattern at any override depth.

interface Alertable {
    String sendAlert(String message);
}

class SecuritySensor {
    private final String zoneName;

    public SecuritySensor(String zoneName) {
        this.zoneName = zoneName;
    }

    String getZoneName() {
        return zoneName;
    }
}

class MotionSensor extends SecuritySensor implements Alertable {
    public MotionSensor(String zoneName) {
        super(zoneName);
    }

    @Override
    public String sendAlert(String message) {
        return "[" + getZoneName() + "] " + message;
    }
}

class DualZoneMotionSensor extends MotionSensor {
    private final String secondZoneName;

    public DualZoneMotionSensor(String zoneName, String secondZoneName) {
        super(zoneName);
        this.secondZoneName = secondZoneName;
    }

    @Override
    public String sendAlert(String message) {
        return super.sendAlert(message) + " [also covering " + secondZoneName + "]";
    }
}

class SmokeDetector implements Alertable {
    private final String deviceId;

    public SmokeDetector(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String sendAlert(String message) {
        return "[" + deviceId + "] " + message;
    }
}

public class P2_HomeSafetyAlertNetwork {
    static void broadcastAll(Alertable[] devices, String message) {
        for (Alertable device : devices) {
            System.out.println(device.sendAlert(message));
        }
    }

    static String getZoneIfMotionSensor(Alertable a) {
        if (a instanceof MotionSensor) {
            return ((MotionSensor) a).getZoneName();
        }
        return "Not a motion sensor";
    }

    public static void main(String[] args) {
        MotionSensor m = new MotionSensor("Living Room");
        System.out.println(m.sendAlert("Motion detected"));

        DualZoneMotionSensor d = new DualZoneMotionSensor("Hallway", "Stairwell");
        System.out.println(d.sendAlert("Motion detected"));

        SmokeDetector s = new SmokeDetector("SD-01");
        System.out.println(s.sendAlert("Smoke detected"));

        broadcastAll(new Alertable[] { m, d, s }, "Evacuate now");

        System.out.println(getZoneIfMotionSensor(m));
        System.out.println(getZoneIfMotionSensor(s));
    }
}
