import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AccessChecker {
    static String classifyAccess(String fieldModifier, String accessorContext) {
        boolean allowed;
        switch (fieldModifier) {
            case "private":
                allowed = accessorContext.equals("SAME_CLASS");
                break;
            case "default":
                allowed = accessorContext.equals("SAME_CLASS") || accessorContext.equals("SAME_PACKAGE");
                break;
            case "protected":
                allowed = accessorContext.equals("SAME_CLASS")
                        || accessorContext.equals("SAME_PACKAGE")
                        || accessorContext.equals("SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE");
                break;
            case "public":
                allowed = true;
                break;
            default:
                throw new IllegalArgumentException("Unknown modifier: " + fieldModifier);
        }
        return allowed ? "ALLOWED" : "DENIED";
    }

    static String describeContext(String accessorContext) {
        String[] words = accessorContext.split("_");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    static String summarizeByModifier(String[][] attempts) {
        String[] order = {"private", "default", "protected", "public"};
        Map<String, int[]> counts = new HashMap<>();
        for (String[] attempt : attempts) {
            boolean allowed = "ALLOWED".equals(classifyAccess(attempt[0], attempt[1]));
            int[] count = counts.computeIfAbsent(attempt[0], k -> new int[2]);
            count[allowed ? 0 : 1]++;
        }
        List<String> parts = new ArrayList<>();
        for (String modifier : order) {
            int[] count = counts.get(modifier);
            if (count != null) {
                parts.add(modifier + ": " + count[0] + " allowed / " + count[1] + " denied");
            }
        }
        return String.join(" | ", parts);
    }

    public static void main(String[] args) {
        System.out.println(classifyAccess("private", "SAME_CLASS"));
        System.out.println(classifyAccess("protected", "DIFFERENT_PACKAGE"));
        System.out.println(classifyAccess("protected", "SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE"));
        System.out.println(classifyAccess("protected", "SUBCLASS_DIFFERENT_PACKAGE_PARENT_TYPE"));
        System.out.println(describeContext("SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE"));
        System.out.println(summarizeByModifier(new String[][]{
                {"private", "SAME_CLASS"}, {"private", "SAME_PACKAGE"},
                {"default", "SAME_PACKAGE"}, {"default", "DIFFERENT_PACKAGE"},
                {"protected", "SAME_PACKAGE"}, {"protected", "SAME_CLASS"},
                {"public", "DIFFERENT_PACKAGE"}
        }));
    }
}