// Polynomial Calculator — adding two exponent-sorted term lists in one merge pass.
// Both lists run in decreasing exponent order, so this is the classic sorted-list merge:
// copy the larger exponent, add coefficients on ties, and skip sums of zero entirely.
// Each input node is visited exactly once and the result stays in decreasing order.
// Complexity: O(m + n) time, O(m + n) space for the new result nodes (inputs untouched).

class Node {
    int coeff;
    int exp;
    Node next;

    Node(int coeff, int exp) {
        this.coeff = coeff;
        this.exp = exp;
    }
}

public class PolynomialCalculator {
    static Node addPolynomials(Node p, Node q) {
        Node dummy = new Node(0, -1);
        Node tail = dummy;
        while (p != null && q != null) {
            if (p.exp > q.exp) {
                tail.next = new Node(p.coeff, p.exp);
                p = p.next;
            } else if (p.exp < q.exp) {
                tail.next = new Node(q.coeff, q.exp);
                q = q.next;
            } else {
                int sum = p.coeff + q.coeff;
                if (sum != 0) {
                    tail.next = new Node(sum, p.exp);
                } else {
                    p = p.next;
                    q = q.next;
                    continue;
                }
                p = p.next;
                q = q.next;
            }
            tail = tail.next;
        }
        while (p != null) {
            tail.next = new Node(p.coeff, p.exp);
            tail = tail.next;
            p = p.next;
        }
        while (q != null) {
            tail.next = new Node(q.coeff, q.exp);
            tail = tail.next;
            q = q.next;
        }
        return dummy.next;
    }

    static Node build(int[][] terms) {
        Node dummy = new Node(0, -1);
        Node tail = dummy;
        for (int[] term : terms) {
            tail.next = new Node(term[0], term[1]);
            tail = tail.next;
        }
        return dummy.next;
    }

    private static String termText(int coeff, int exp, boolean first) {
        StringBuilder out = new StringBuilder();
        if (!first) {
            out.append(coeff < 0 ? " - " : " + ");
        } else if (coeff < 0) {
            out.append("-");
        }
        int magnitude = Math.abs(coeff);
        if (exp == 0) {
            out.append(magnitude);
        } else {
            if (magnitude != 1) {
                out.append(magnitude);
            }
            out.append("x");
            if (exp != 1) {
                out.append("^").append(exp);
            }
        }
        return out.toString();
    }

    static String show(Node head) {
        if (head == null) {
            return "0";
        }
        StringBuilder out = new StringBuilder();
        boolean first = true;
        for (Node cur = head; cur != null; cur = cur.next) {
            out.append(termText(cur.coeff, cur.exp, first));
            first = false;
        }
        return out.toString();
    }

    public static void main(String[] args) {
        Node p1 = build(new int[][] { { 5, 3 }, { 4, 2 }, { 2, 0 } });
        Node q1 = build(new int[][] { { 5, 2 }, { -3, 1 }, { 1, 0 } });
        System.out.println(show(addPolynomials(p1, q1)));

        Node p2 = build(new int[][] { { 3, 2 }, { 1, 0 } });
        Node q2 = build(new int[][] { { -3, 2 }, { 4, 1 } });
        System.out.println(show(addPolynomials(p2, q2)));
    }
}
