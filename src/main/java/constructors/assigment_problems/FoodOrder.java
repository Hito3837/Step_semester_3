class FoodOrder {
    private final String studentName;
    private final String dishName;
    private boolean delivered;

    public FoodOrder(String studentName, String dishName) {
        if (isBlank(studentName)) {
            throw new IllegalArgumentException("studentName or dishName cannot be blank");
        }
        if (isBlank(dishName)) {
            throw new IllegalArgumentException("dishName cannot be blank");
        }
        this.studentName = studentName.trim();
        this.dishName = dishName.trim();
        this.delivered = false;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    void markDelivered() {
        if (delivered) {
            System.out.println("WARNING: duplicate delivery attempt blocked - order was already delivered");
        } else {
            delivered = true;
            System.out.println("Order marked as delivered");
        }
    }

    static void processBatch(String[][] rawOrders) {
        int valid = 0;
        int rejected = 0;
        for (String[] raw : rawOrders) {
            try {
                new FoodOrder(raw[0], raw[1]);
                valid++;
            } catch (IllegalArgumentException e) {
                rejected++;
            }
        }
        System.out.println("Valid: " + valid + " | Rejected: " + rejected);
    }

    public static void main(String[] args) {
        FoodOrder order = new FoodOrder("Ravi", "Paneer Butter Masala");
        order.markDelivered();
        order.markDelivered();

        processBatch(new String[][]{
                {"Ravi", "Paneer Butter Masala"},
                {"", "Chole Bhature"},
                {"Meera", "   "},
                {"Divya", "Veg Biryani"}
        });
    }
}