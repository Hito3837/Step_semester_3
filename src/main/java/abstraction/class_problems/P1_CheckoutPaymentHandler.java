// Checkout Payment Handler — abstract payments, final Txn ids, compile-time overloads.
// PaymentMethod is abstract, so `new PaymentMethod()` cannot compile: only a concrete
// way of paying (CreditCardPayment / CashPayment) can ever be constructed.
// The shared static counter + final transactionId live once in PaymentMethod's own
// constructor (every subclass reaches it via super()), and the two-argument overload
// simply reuses the one-argument version and glues the note on the end.

abstract class PaymentMethod {
    private static int nextTxn = 1001;
    private final String transactionId;

    PaymentMethod() {
        this.transactionId = "TXN-" + (nextTxn++);
    }

    public abstract String processPayment(double amount);

    // Overload: same name, extra parameter — resolved at compile time by argument count.
    String processPayment(double amount, String note) {
        return processPayment(amount) + " (" + note + ")";
    }

    public String getTransactionId() {
        return transactionId;
    }
}

class CreditCardPayment extends PaymentMethod {
    private final String cardNumberLastFour;

    public CreditCardPayment(String cardNumberLastFour) {
        super();
        this.cardNumberLastFour = cardNumberLastFour;
    }

    @Override
    public String processPayment(double amount) {
        return "Charged $" + amount + " to card ending " + cardNumberLastFour
                + " - Txn " + getTransactionId();
    }
}

class CashPayment extends PaymentMethod {
    public CashPayment() {
        super();
    }

    @Override
    public String processPayment(double amount) {
        return "Received $" + amount + " in cash - Txn " + getTransactionId();
    }
}

public class P1_CheckoutPaymentHandler {
    static void printConfirmation(PaymentMethod payment, double amount) {
        System.out.println(payment.processPayment(amount));
    }

    public static void main(String[] args) {
        CreditCardPayment cc = new CreditCardPayment("4471");
        System.out.println(cc.processPayment(250.0));

        CashPayment cash = new CashPayment();
        System.out.println(cash.processPayment(40.0));

        System.out.println(cc.processPayment(250.0, "Birthday gift"));

        PaymentMethod ref = cc; // upcasting: a CreditCardPayment stored as its parent type
        printConfirmation(ref, 250.0);
    }
}
