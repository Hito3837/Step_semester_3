// Customer Registry — the equals/hashCode contract in hash-based collections.
// Two Customer objects with the same id AND name represent one real-world customer, so
// equals() compares content and hashCode() is built from the same fields. Break that pairing
// (equals without a matching hashCode) and equal customers would land in different HashSet
// buckets — duplicates counted, lookups missed. (The Customer name is intentionally reused
// from the Week 8 rental file; per-file compilation keeps each program self-contained.)

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class Customer {
    private final int id;
    private final String name;

    Customer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) o;
        return id == other.id && Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

class CustomerRegistry {
    private final Set<Customer> customers = new HashSet<>();

    boolean add(Customer customer) {
        return customers.add(customer);
    }

    boolean contains(Customer customer) {
        return customers.contains(customer);
    }

    int uniqueCount() {
        return customers.size();
    }
}

public class P2_CustomerRegistry {
    public static void main(String[] args) {
        CustomerRegistry registry = new CustomerRegistry();

        System.out.println(registry.add(new Customer(101, "Asha")));
        System.out.println(registry.add(new Customer(101, "Asha")) + " (duplicate rejected)");
        System.out.println(registry.add(new Customer(102, "Ravi")));
        System.out.println("unique count " + registry.uniqueCount());
        System.out.println("contains: " + registry.contains(new Customer(101, "Asha")));
    }
}
