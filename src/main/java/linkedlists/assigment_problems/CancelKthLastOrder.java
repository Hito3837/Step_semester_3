// Cancelling the k-th Last Order — single-pass removal with a lead/trail gap.
// The lead pointer starts k + 1 steps ahead of the trail pointer (which begins at a dummy
// node before the head); when the lead falls off the end, the trail sits exactly before the
// target, so one relink removes it. The dummy makes head-removal (sample 2) need no special
// case: the trail simply starts before the head like anywhere else.
// Complexity: O(n) time in one pass, O(1) extra space (dummy + two pointers).

class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
    }
}

public class CancelKthLastOrder {
    static Node removeKthFromEnd(Node head, int k) {
        Node dummy = new Node(0);
        dummy.next = head;
        Node lead = dummy;
        for (int i = 0; i <= k; i++) {
            lead = lead.next;
        }
        Node trail = dummy;
        while (lead != null) {
            lead = lead.next;
            trail = trail.next;
        }
        trail.next = trail.next.next;
        return dummy.next;
    }

    static Node build(int... values) {
        Node dummy = new Node(0);
        Node tail = dummy;
        for (int value : values) {
            tail.next = new Node(value);
            tail = tail.next;
        }
        return dummy.next;
    }

    static String show(Node head) {
        StringBuilder out = new StringBuilder();
        for (Node cur = head; cur != null; cur = cur.next) {
            if (out.length() > 0) {
                out.append(" -> ");
            }
            out.append(cur.data);
        }
        return out.toString();
    }

    public static void main(String[] args) {
        System.out.println(show(removeKthFromEnd(build(10, 20, 30, 40, 50), 2)));
        System.out.println(show(removeKthFromEnd(build(10, 20, 30, 40, 50), 5)));
    }
}
