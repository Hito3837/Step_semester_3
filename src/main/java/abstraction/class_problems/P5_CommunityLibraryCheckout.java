// Community Library Checkout System — siblings adopting different capabilities.
// Magazine shares LibraryItem as a parent with Textbook, yet choosing NOT to implement
// Reservable is exactly the point: a shared parent never forces siblings to share
// capabilities. reserveIfSupported takes a plain Object on purpose — Reservable and
// LibraryItem are fully independent, so it works on anything, LibraryItem or not.

abstract class LibraryItem {
    private static int nextItem = 1001;
    private final String itemId;

    LibraryItem() {
        this.itemId = "LIB-" + (nextItem++);
    }

    public abstract int getLoanPeriodDays();

    String getItemId() {
        return itemId;
    }
}

interface Renewable {
    String renew();
}

interface Reservable {
    String reserve();
}

class Textbook extends LibraryItem implements Renewable, Reservable {
    private final String title;

    public Textbook(String title) {
        super();
        this.title = title;
    }

    @Override
    public int getLoanPeriodDays() {
        return 14;
    }

    @Override
    public String renew() {
        return title + " renewed";
    }

    @Override
    public String reserve() {
        return title + " reserved";
    }
}

class Magazine extends LibraryItem implements Renewable {
    private final String title;

    public Magazine(String title) {
        super();
        this.title = title;
    }

    @Override
    public int getLoanPeriodDays() {
        return 7;
    }

    @Override
    public String renew() {
        return title + " renewed";
    }
}

class DigitalPass implements Renewable {
    private final String resourceName;

    public DigitalPass(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public String renew() {
        return resourceName + " renewed";
    }
}

public class P5_CommunityLibraryCheckout {
    static void processCheckouts(LibraryItem[] items) {
        for (LibraryItem item : items) {
            System.out.println(item.getLoanPeriodDays());
        }
    }

    static String reserveIfSupported(Object o) {
        if (o instanceof Reservable) {
            return ((Reservable) o).reserve();
        }
        return "Reservation not supported";
    }

    public static void main(String[] args) {
        Textbook t = new Textbook("Java Fundamentals");
        System.out.println(t.getLoanPeriodDays());
        System.out.println(t.renew());
        System.out.println(t.reserve());

        Magazine m = new Magazine("Tech Monthly");
        System.out.println(reserveIfSupported(m));

        DigitalPass d = new DigitalPass("E-Journal Access");
        System.out.println(reserveIfSupported(d));

        LibraryItem ref = t; // upcasting: Textbook stored as its parent type
        System.out.println(reserveIfSupported(ref));

        processCheckouts(new LibraryItem[] { t, m });
    }
}
