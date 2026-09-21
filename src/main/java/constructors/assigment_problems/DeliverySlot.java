class DeliverySlot {
    private final String orderId;
    private final String timeSlot;

    public DeliverySlot(String orderId, String timeSlot) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("orderId cannot be blank");
        }
        this.orderId = orderId.trim();
        this.timeSlot = timeSlot == null ? "ASAP" : timeSlot;
    }

    public DeliverySlot(String orderId) {
        this(orderId, "ASAP");
    }

    boolean isPeakHour() {
        switch (timeSlot) {
            case "12:00-13:00":
            case "13:00-14:00":
            case "19:00-20:00":
            case "20:00-21:00":
                return true;
            default:
                return false;
        }
    }

    public static void main(String[] args) {
        System.out.println(new DeliverySlot("ORD101", "13:00-14:00").isPeakHour());
        System.out.println(new DeliverySlot("ORD102").isPeakHour());
    }
}