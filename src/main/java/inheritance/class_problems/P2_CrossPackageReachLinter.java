class AccessRuleEngine {
    /* Problem 2 extends the linter from Problem 1 to the one genuinely tricky row in the
       visibility matrix: a protected member reached by a subclass in a different package.
       The ICU module (different package) extends PatientRecord, and engineers keep getting
       this wrong in review. */
    static String classifyAccess(String fieldModifier, String accessorContext) {
        switch (fieldModifier) {
            case "public":
                return "ALLOWED";
            case "private":
                /* private never escapes the owning class, so a subclass in another package
                   can no more reach it than a plain unrelated caller can. */
                return "SAME_CLASS".equals(accessorContext) ? "ALLOWED" : "DENIED";
            case "default":
                /* Package-private never crosses package boundaries either: even a subclass
                   sitting elsewhere cannot see it. */
                return "SAME_CLASS".equals(accessorContext) || "SAME_PACKAGE".equals(accessorContext)
                        ? "ALLOWED" : "DENIED";
            case "protected":
                if ("SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE".equals(accessorContext)) {
                    /* The reference is declared as the subclass type itself — the subclass
                       may access the protected member it inherited. */
                    return "ALLOWED";
                }
                if ("SUBCLASS_DIFFERENT_PACKAGE_PARENT_TYPE".equals(accessorContext)) {
                    /* Java doesn't ask "is this object secretly a subclass at runtime?" — it
                       asks "what is the declared type of the variable right now?" Accessed
                       through a parent-typed reference, protected is NOT reachable from a
                       different package even for a genuine subclass instance. */
                    return "DENIED";
                }
                /* The three plain contexts behave exactly like default. */
                return "SAME_CLASS".equals(accessorContext) || "SAME_PACKAGE".equals(accessorContext)
                        ? "ALLOWED" : "DENIED";
            default:
                return "DENIED";
        }
    }

    /* Turns an underscore-separated code into a readable, title-cased sentence. */
    static String describeContext(String accessorContext) {
        if (accessorContext == null || accessorContext.isEmpty()) {
            return "";
        }
        String[] parts = accessorContext.split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(classifyAccess("protected", "SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE"));
        System.out.println(classifyAccess("protected", "SUBCLASS_DIFFERENT_PACKAGE_PARENT_TYPE"));
        System.out.println(describeContext("SUBCLASS_DIFFERENT_PACKAGE_PARENT_TYPE"));
        System.out.println(classifyAccess("private", "SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE"));
        System.out.println(describeContext("SAME_PACKAGE"));
    }
}