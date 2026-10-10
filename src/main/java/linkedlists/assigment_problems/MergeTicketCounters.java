// Merging Two Ticket Counters — in-place merge of two sorted singly linked lists.
// A tail pointer always attaches the smaller current node (duplicates kept: a <= b takes
// from the left first); when one list runs out, the rest of the other is attached whole.
// Only one dummy node is ever created — every other node is relinked, never copied.
// Complexity: O(m + n) time, O(1) extra space. Copying both lists into an array and sorting
// would cost O((m + n) log(m + n)) time plus O(m + n) space.

class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
    }
}

public class MergeTicketCounters {
    static Node mergeSorted(Node a, Node b) {
        Node dummy = new Node(0);
        Node tail = dummy;
        while (a != null && b != null) {
            if (a.data <= b.data) {
                tail.next = a;
                a = a.next;
            } else {
                tail.next = b;
                b = b.next;
            }
            tail = tail.next;
        }
        tail.next = (a != null) ? a : b;
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
        if (head == null) {
            return "(empty)";
        }
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
        System.out.println(show(mergeSorted(build(3, 8, 15), build(5, 8, 12, 20))));
        System.out.println(show(mergeSorted(null, build(4))));
    }
}
