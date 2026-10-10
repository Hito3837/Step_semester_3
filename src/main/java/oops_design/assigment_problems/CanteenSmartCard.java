import java.util.*;

// The Campus Canteen Smart Card — prepaid card with transaction-derived balance and plan pricing.
// Classes / relationships / multiplicities:
//   SmartCard (1) ---- (*) CardTransaction   : COMPOSITION — every balance change is a recorded
//                                            transaction; the balance is DERIVED as the sum of
//                                            transactions, so 'balance = sum(transactions)' holds
//                                            structurally and the balance can never be touched directly
//   SmartCard (1) ---- (*) CardPurchase      : each purchase is refundable exactly once
//   PricingPlan <|-- DayScholarPlan, HostellerPlan, StaffPlan (interface; new plans plug in with
//                no change to the purchase/refund workflow)
//   Card state: ACTIVE <-> BLOCKED; a blocked card rejects every top-up and purchase.
// Rules: top-up minimum Rs.100, balance ceiling Rs.5000, purchases need sufficient balance,
// refunds return the exact charged amount and are one-time.

interface PricingPlan {
    String planName();

    double priceFor(double mrp);
}

class DayScholarPlan implements PricingPlan {
    @Override
    public String planName() {
        return "Day Scholar";
    }

    @Override
    public double priceFor(double mrp) {
        return mrp;
    }
}

class HostellerPlan implements PricingPlan {
    @Override
    public String planName() {
        return "Hosteller";
    }

    @Override
    public double priceFor(double mrp) {
        return mrp * 90.0 / 100.0;
    }
}

class StaffPlan implements PricingPlan {
    @Override
    public String planName() {
        return "Staff";
    }

    @Override
    public double priceFor(double mrp) {
        return mrp * 80.0 / 100.0;
    }
}

class CardTransaction {
    private final double amount; // + top-up/refund, - purchase

    CardTransaction(double amount) {
        this.amount = amount;
    }

    double getAmount() {
        return amount;
    }
}

class CardPurchase {
    private final String itemName;
    private final double charged;
    private boolean refunded;

    CardPurchase(String itemName, double charged) {
        this.itemName = itemName;
        this.charged = charged;
    }

    String getItemName() {
        return itemName;
    }

    double getCharged() {
        return charged;
    }

    boolean isRefunded() {
        return refunded;
    }

    void markRefunded() {
        refunded = true;
    }
}

class SmartCard {
    private static final double MIN_TOPUP = 100.0;
    private static final double MAX_BALANCE = 5000.0;

    private final String cardId;
    private final PricingPlan plan;
    private final List<CardTransaction> ledger = new ArrayList<>();
    private boolean blocked;

    SmartCard(String cardId, PricingPlan plan) {
        this.cardId = cardId;
        this.plan = plan;
    }

    // The ONLY reader of money: derived from the ledger, never stored, never settable.
    double balance() {
        double sum = 0.0;
        for (CardTransaction entry : ledger) {
            sum += entry.getAmount();
        }
        return sum;
    }

    private static String money(double value) {
        return String.format("%.2f", value);
    }

    void block() {
        blocked = true;
    }

    void unblock() {
        blocked = false;
    }

    void topUp(double amount) {
        if (blocked) {
            System.out.println("Top-up rejected: card " + cardId + " is blocked.");
            return;
        }
        if (amount < MIN_TOPUP) {
            System.out.println("Top-up rejected: minimum top-up is \u20B9100.00.");
            return;
        }
        if (balance() + amount > MAX_BALANCE) {
            System.out.println("Top-up rejected: balance cannot exceed \u20B95000.00.");
            return;
        }
        ledger.add(new CardTransaction(amount));
        System.out.println(cardId + " topped up with \u20B9" + money(amount)
                + ". Balance: \u20B9" + money(balance()) + ".");
    }

    CardPurchase purchase(String itemName, double mrp) {
        if (blocked) {
            System.out.println("Purchase failed: card " + cardId + " is blocked.");
            return null;
        }
        double charged = plan.priceFor(mrp);
        if (charged > balance()) {
            System.out.println("Purchase failed: Insufficient balance (required \u20B9"
                    + money(charged) + ", available \u20B9" + money(balance()) + ").");
            return null;
        }
        ledger.add(new CardTransaction(-charged));
        CardPurchase purchase = new CardPurchase(itemName, charged);
        System.out.println(itemName + " purchased for \u20B9" + money(charged)
                + ". Balance: \u20B9" + money(balance()) + ".");
        return purchase;
    }

    void refund(CardPurchase purchase) {
        if (purchase == null) {
            return;
        }
        if (purchase.isRefunded()) {
            System.out.println("Refund rejected: " + purchase.getItemName()
                    + " has already been refunded.");
            return;
        }
        purchase.markRefunded();
        ledger.add(new CardTransaction(purchase.getCharged()));
        System.out.println("Refund of \u20B9" + money(purchase.getCharged()) + " for "
                + purchase.getItemName() + " processed. Balance: \u20B9" + money(balance()) + ".");
    }

    void printMiniStatement() {
        StringBuilder out = new StringBuilder("Mini-statement for " + cardId + ": ");
        for (int i = 0; i < ledger.size(); i++) {
            double amount = ledger.get(i).getAmount();
            if (i > 0) {
                out.append(", ");
            }
            out.append(amount < 0 ? "-" : "+").append(money(Math.abs(amount)));
        }
        out.append(" = \u20B9").append(money(balance())).append(".");
        System.out.println(out);
    }
}

public class CanteenSmartCard {
    public static void main(String[] args) {
        // Force UTF-8 bytes for ₹ regardless of the platform default charset.
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        SmartCard card = new SmartCard("C-2045", new HostellerPlan());

        card.topUp(500);
        CardPurchase thali = card.purchase("Veg Thali", 120);
        card.purchase("Cold Coffee", 60);
        card.purchase("Snacks Combo", 400);
        card.refund(thali);
        card.refund(thali);
        card.printMiniStatement();
    }
}
