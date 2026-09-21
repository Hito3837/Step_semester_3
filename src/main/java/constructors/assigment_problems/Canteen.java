class Canteen {
    private final String canteenCode;
    private final String canteenName;
    private final int trustScore;

    public Canteen(String canteenCode, String canteenName, int trustScore) {
        if (canteenCode == null || canteenCode.trim().isEmpty()
                || canteenName == null || canteenName.trim().isEmpty()) {
            throw new IllegalArgumentException("canteenCode and canteenName cannot be blank");
        }
        if (trustScore < 0 || trustScore > 5) {
            throw new IllegalArgumentException("trustScore must be between 0 and 5");
        }
        this.canteenCode = canteenCode.trim();
        this.canteenName = canteenName.trim();
        this.trustScore = trustScore;
    }

    public Canteen(String canteenCode, String canteenName) {
        this(canteenCode, canteenName, 3);
    }

    int compareTo(Canteen other) {
        int scoreDiff = Integer.compare(other.trustScore, this.trustScore);
        if (scoreDiff != 0) {
            return scoreDiff;
        }
        int codeDiff = this.canteenCode.compareToIgnoreCase(other.canteenCode);
        if (codeDiff != 0) {
            return codeDiff;
        }
        return Integer.compare(this.canteenName.length(), other.canteenName.length());
    }

    static Canteen[] rankCanteens(Canteen[] canteens) {
        Canteen[] result = canteens.clone();
        for (int i = 0; i < result.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < result.length; j++) {
                if (result[j].compareTo(result[min]) < 0) {
                    min = j;
                }
            }
            Canteen temp = result[i];
            result[i] = result[min];
            result[min] = temp;
        }
        return result;
    }

    public String getCode() {
        return canteenCode;
    }

    public static void main(String[] args) {
        Canteen[] input = new Canteen[]{
                new Canteen("HB3-C", "Spice Junction", 3),
                new Canteen("hb1-c", "Grand Mess", 5),
                new Canteen("HB2-C", "Southern Treats")
        };
        for (Canteen c : rankCanteens(input)) {
            System.out.println(c.getCode());
        }
    }
}