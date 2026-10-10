import java.util.*;

// Food Order and Flexible Payment System.
// Classes / relationships / multiplicities:
//   Customer (1) ---- (*) Order            : a customer may place multiple orders
//   Restaurant (1) ---- (*) FoodItem       : a restaurant offers many items
//   Order (1) ---- (1..*) LineItem         : COMPOSITION — LineItems are created/managed exclusively
//                                            by their Order and die with it; an order with zero items
//                                            cannot be placed (invariant enforced in placeOrder)
//   IPaymentMethod <|-- CreditCardPayment, DigitalWalletPayment, CashOnDelivery (realization;
//                      new methods plug in with zero changes to Order — dependency on abstractions)
//   NotificationService broadcasts order events ('Order Placed', 'Payment Successful') to the customer.
// Object interactions: Customer -> Order.addItem() (builds LineItems) -> Order.placeOrder(payment) ->
// IPaymentMethod.pay(total) -> Order updates status (Paid / Pending Payment) ->
// NotificationService relays the outcome to the Customer.

class FoodItem {
    private final String name;
    private final double price;

    FoodItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    String getName() {
        return name;
    }

    double getPrice() {
        return price;
    }
}

class Restaurant {
    private final String name;
    private final List<FoodItem> menu = new ArrayList<>();

    Restaurant(String name) {
        this.name = name;
    }

    void addToMenu(FoodItem item) {
        menu.add(item);
    }

    String getName() {
        return name;
    }
}

class LineItem {
    private final FoodItem item;
    private final int quantity;

    LineItem(FoodItem item, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        this.item = item;
        this.quantity = quantity;
    }

    double lineTotal() {
        return item.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return item.getName() + " (Qty " + quantity + ")";
    }
}

class FoodCustomer {
    private final String name;

    FoodCustomer(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}

interface IPaymentMethod {
    String displayName();

    boolean pay(double amount);
}

class CreditCardPayment implements IPaymentMethod {
    @Override
    public String displayName() {
        return "Credit Card";
    }

    @Override
    public boolean pay(double amount) {
        return true; // simulated gateway approval
    }
}

class DigitalWalletPayment implements IPaymentMethod {
    private final boolean simulateSuccess;

    DigitalWalletPayment(boolean simulateSuccess) {
        this.simulateSuccess = simulateSuccess;
    }

    @Override
    public String displayName() {
        return "Digital Wallet";
    }

    @Override
    public boolean pay(double amount) {
        return simulateSuccess; // simulated gateway verdict
    }
}

class CashOnDeliveryPayment implements IPaymentMethod {
    @Override
    public String displayName() {
        return "Cash on Delivery";
    }

    @Override
    public boolean pay(double amount) {
        return true;
    }
}

class NotificationService {
    static void notify(FoodCustomer customer, String message) {
        System.out.println("Notification: " + message + ".");
    }
}

class FoodOrder {
    private static int nextId = 123;
    private int id; // assigned only when a non-empty order is actually placed
    private final FoodCustomer customer;
    private final List<LineItem> items = new ArrayList<>();
    private String status = "Created";

    FoodOrder(FoodCustomer customer) {
        this.customer = customer;
        System.out.println("Order created.");
    }

    void addItem(FoodItem item, int quantity) {
        LineItem line = new LineItem(item, quantity);
        items.add(line);
        System.out.println("Added " + line + ".");
    }

    double total() {
        double sum = 0.0;
        for (LineItem line : items) {
            sum += line.lineTotal();
        }
        return sum;
    }

    void placeOrder(IPaymentMethod payment) {
        if (items.isEmpty()) {
            System.out.println("Cannot place order: Order must contain at least one item.");
            return;
        }
        if (id == 0) {
            id = nextId++;
        }
        System.out.println("Order placed.");
        boolean ok = payment.pay(total());
        if (ok) {
            status = "Paid";
            System.out.println("Payment via " + payment.displayName() + " successful. Order status: " + status + ".");
            NotificationService.notify(customer, "Order #" + id + " placed and paid");
        } else {
            status = "Pending Payment";
            System.out.println("Payment via " + payment.displayName() + " failed. Order status: " + status + ".");
            NotificationService.notify(customer, "Order #" + id + " placed, awaiting payment");
        }
    }
}

public class P5_FoodOrderPaymentSystem {
    public static void main(String[] args) {
        Restaurant bistro = new Restaurant("City Bistro");
        FoodItem pizza = new FoodItem("Pizza", 12.50);
        FoodItem soda = new FoodItem("Soda", 2.50);
        FoodItem burger = new FoodItem("Burger", 8.00);
        bistro.addToMenu(pizza);
        bistro.addToMenu(soda);
        bistro.addToMenu(burger);

        FoodCustomer customer = new FoodCustomer("Aarav");

        FoodOrder first = new FoodOrder(customer);
        first.addItem(pizza, 2);
        first.addItem(soda, 1);

        FoodOrder empty = new FoodOrder(customer);
        empty.placeOrder(new CreditCardPayment());

        first.placeOrder(new CreditCardPayment());

        FoodOrder second = new FoodOrder(customer);
        second.addItem(burger, 1);
        second.placeOrder(new DigitalWalletPayment(false));
    }
}
