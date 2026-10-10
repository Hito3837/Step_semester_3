// Smart Home Platform — capability interfaces with command rejection.
// Every device IS-A Device (id + on/off state). Extra capabilities are CAN-DO interfaces, so
// Light (dim + schedule), Fan (schedule) and Plug (energy) differ only in their implements
// clauses. The command processor tests each capability with instanceof and rejects anything
// the target device does not support, instead of failing silently.

interface Dimmable {
    String dim(int level);
}

interface Schedulable {
    String schedule(String time);
}

interface EnergyMonitor {
    String energy();
}

abstract class Device {
    private final String id;
    private boolean on;

    Device(String id) {
        this.id = id;
    }

    String getId() {
        return id;
    }

    String turnOn() {
        on = true;
        return id + " is ON";
    }
}

class Light extends Device implements Dimmable, Schedulable {
    Light(String id) {
        super(id);
    }

    @Override
    public String dim(int level) {
        return getId() + " dimmed to " + level;
    }

    @Override
    public String schedule(String time) {
        return getId() + " scheduled " + time;
    }
}

class Fan extends Device implements Schedulable {
    Fan(String id) {
        super(id);
    }

    @Override
    public String schedule(String time) {
        return getId() + " scheduled " + time;
    }
}

class Plug extends Device implements EnergyMonitor {
    private final int kwh;

    Plug(String id, int kwh) {
        super(id);
        this.kwh = kwh;
    }

    @Override
    public String energy() {
        return getId() + " energy " + kwh + " kWh";
    }
}

public class P1_SmartHomePlatform {
    static void command(Device device, String action, String arg) {
        switch (action) {
            case "ON":
                System.out.println(device.turnOn());
                break;
            case "DIM":
                if (device instanceof Dimmable) {
                    System.out.println(((Dimmable) device).dim(Integer.parseInt(arg)));
                } else {
                    System.out.println(device.getId() + " rejected: DIM unsupported");
                }
                break;
            case "SCHEDULE":
                if (device instanceof Schedulable) {
                    System.out.println(((Schedulable) device).schedule(arg));
                } else {
                    System.out.println(device.getId() + " rejected: SCHEDULE unsupported");
                }
                break;
            case "ENERGY":
                if (device instanceof EnergyMonitor) {
                    System.out.println(((EnergyMonitor) device).energy());
                } else {
                    System.out.println(device.getId() + " rejected: ENERGY unsupported");
                }
                break;
            default:
                System.out.println(device.getId() + " rejected: unknown command");
                break;
        }
    }

    public static void main(String[] args) {
        Light l1 = new Light("L1");
        Fan f1 = new Fan("F1");
        Plug p1 = new Plug("P1", 12);

        command(l1, "ON", null);
        command(l1, "DIM", "40");
        command(f1, "DIM", "30");
        command(f1, "SCHEDULE", "22:00");
        command(p1, "ENERGY", null);
        command(l1, "ENERGY", null);
    }
}
