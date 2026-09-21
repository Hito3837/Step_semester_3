class BusRoute {
    private final String routeCode;
    private final String routeName;
    private final int priority;

    /* this resolves the field/parameter name clash on all three arguments. */
    public BusRoute(String routeCode, String routeName, int priority) {
        if (routeCode == null || routeCode.trim().isEmpty() || routeName == null || routeName.trim().isEmpty()) {
            throw new IllegalArgumentException("routeCode and routeName cannot be blank");
        }
        this.routeCode = routeCode;
        this.routeName = routeName;
        this.priority = priority;
    }

    /* Chains to the full constructor with a sensible default: a route built without an
       explicit priority gets the lowest one, so it ranks last among named priorities. */
    public BusRoute(String routeCode, String routeName) {
        this(routeCode, routeName, 0);
    }

    /* Signed comparison following the Comparable convention (negative = "less than").
       Tie-breaking order: primary = the clearly important measure (higher priority first);
       secondary = route code, compared case-insensitively (so "RT205L" vs "rt201j" are
       compared without altering how either code is stored or displayed);
       final = 0, which makes equal routes retain their arrival order (stable sort). */
    int compareTo(BusRoute other) {
        if (this.priority != other.priority) {
            return Integer.compare(other.priority, this.priority);
        }
        return this.routeCode.compareToIgnoreCase(other.routeCode);
    }

    static BusRoute[] rankRoutes(BusRoute[] routes) {
        BusRoute[] result = routes.clone();
        for (int i = 1; i < result.length; i++) {
            BusRoute key = result[i];
            int j = i - 1;
            while (j >= 0 && result[j].compareTo(key) > 0) {
                result[j + 1] = result[j];
                j--;
            }
            result[j + 1] = key;
        }
        return result;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public static void main(String[] args) {
        BusRoute[] routes = {
                new BusRoute("RT205L", "Airport Express", 3),
                new BusRoute("rt201j", "City Central", 4),
                new BusRoute("RT299T", "Night Service")
        };
        StringBuilder sb = new StringBuilder("[");
        for (BusRoute route : rankRoutes(routes)) {
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append('"').append(route.routeCode).append('"');
        }
        sb.append("]");
        System.out.println(sb);
    }
}