import java.util.*;

// The Smart Lab Control Panel — composition over inheritance for device capabilities.
// Classes / relationships / multiplicities:
//   SmartDevice (1) ---- (*) Capability     : COMPOSITION — a device holds any combination of
//                                            capability objects; capabilities can be added at runtime,
//                                            so no 2^n subclass explosion (Power x Brightness x Temperature
//                                            would need 8 subclasses; each new capability doubles it)
//   Capability <|-- PowerCapability, BrightnessCapability, TemperatureCapability (interface;
//                a future TimerCapability plugs in with no change to Device or Scene)
//   LabScene (1) ---- (*) SceneStep         : a scene is an ordered list of (capability, value) steps
// Each capability guards its own valid range and rejects out-of-range values without changing state,
// so every device is valid after every command. A scene step applies to every device that HAS the
// capability; devices without it are skipped.

interface Capability {
    String key();

    String displayName();

    boolean trySet(int value);

    String appliedText(String deviceName);

    String rangeText();
}

class PowerCapability implements Capability {
    private boolean on;

    @Override
    public String key() {
        return "power";
    }

    @Override
    public String displayName() {
        return "Power";
    }

    @Override
    public boolean trySet(int value) {
        if (value != 0 && value != 1) {
            return false;
        }
        on = value == 1;
        return true;
    }

    @Override
    public String appliedText(String deviceName) {
        return deviceName + ": " + (on ? "ON." : "OFF.");
    }

    @Override
    public String rangeText() {
        return "power must be ON or OFF";
    }
}

class BrightnessCapability implements Capability {
    private int percent = 100;

    @Override
    public String key() {
        return "brightness";
    }

    @Override
    public String displayName() {
        return "Brightness";
    }

    @Override
    public boolean trySet(int value) {
        if (value < 0 || value > 100) {
            return false;
        }
        percent = value;
        return true;
    }

    @Override
    public String appliedText(String deviceName) {
        return deviceName + ": brightness set to " + percent + "%.";
    }

    @Override
    public String rangeText() {
        return "brightness must be between 0% and 100%";
    }
}

class TemperatureCapability implements Capability {
    private int celsius = 24;

    @Override
    public String key() {
        return "temperature";
    }

    @Override
    public String displayName() {
        return "Temperature";
    }

    @Override
    public boolean trySet(int value) {
        if (value < 16 || value > 30) {
            return false;
        }
        celsius = value;
        return true;
    }

    @Override
    public String appliedText(String deviceName) {
        return deviceName + ": temperature set to " + celsius + "\u00B0C.";
    }

    @Override
    public String rangeText() {
        return "temperature must be between 16\u00B0C and 30\u00B0C";
    }
}

class SmartDevice {
    private final String name;
    private final Map<String, Capability> capabilities = new LinkedHashMap<>();

    SmartDevice(String name) {
        this.name = name;
    }

    SmartDevice(String name, Capability... initial) {
        this.name = name;
        for (Capability capability : initial) {
            capabilities.put(capability.key(), capability);
        }
    }

    String getName() {
        return name;
    }

    void addCapability(Capability capability) {
        capabilities.put(capability.key(), capability);
        System.out.println(name + ": " + capability.displayName() + " capability added.");
    }

    boolean hasCapability(String key) {
        return capabilities.containsKey(key);
    }

    // Returns 1 when the step applied, 0 when skipped/rejected. Rejections never mutate state
    // because the capability validates BEFORE storing the value.
    int applyStep(String key, int value, boolean silentSkip) {
        Capability capability = capabilities.get(key);
        if (capability == null) {
            return 0;
        }
        if (!capability.trySet(value)) {
            if (!silentSkip) {
                System.out.println("Rejected: " + name + " " + capability.rangeText() + ".");
            }
            return 0;
        }
        System.out.println(capability.appliedText(name));
        return 1;
    }

    void setValue(String key, int value) {
        applyStep(key, value, false);
    }
}

class SceneStep {
    private final String capabilityKey;
    private final int value;

    SceneStep(String capabilityKey, int value) {
        this.capabilityKey = capabilityKey;
        this.value = value;
    }

    String getCapabilityKey() {
        return capabilityKey;
    }

    int getValue() {
        return value;
    }
}

class LabScene {
    private final String name;
    private final List<SceneStep> steps = new ArrayList<>();

    LabScene(String name) {
        this.name = name;
    }

    void addStep(String capabilityKey, int value) {
        steps.add(new SceneStep(capabilityKey, value));
    }

    void run(List<SmartDevice> devices) {
        System.out.println("Scene '" + name + "' started.");
        int applied = 0;
        for (SceneStep step : steps) {
            for (SmartDevice device : devices) {
                applied += device.applyStep(step.getCapabilityKey(), step.getValue(), true);
            }
        }
        System.out.println("Scene '" + name + "' completed: " + applied + " actions applied.");
    }
}

public class SmartLabControlPanel {
    public static void main(String[] args) {
        // Force UTF-8 bytes for °C regardless of the platform default charset.
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        SmartDevice labAc = new SmartDevice("Lab AC",
                new PowerCapability(), new TemperatureCapability());

        SmartDevice ceilingLights = new SmartDevice("Ceiling Lights",
                new PowerCapability(), new BrightnessCapability());

        SmartDevice projector = new SmartDevice("Projector", new PowerCapability());

        List<SmartDevice> lab = Arrays.asList(labAc, ceilingLights, projector);

        LabScene lectureMode = new LabScene("Lecture Mode");
        lectureMode.addStep("power", 1);
        lectureMode.addStep("brightness", 40);
        lectureMode.addStep("temperature", 24);
        lectureMode.run(lab);

        labAc.setValue("temperature", 12);

        projector.addCapability(new BrightnessCapability());
        projector.setValue("brightness", 70);
    }
}
