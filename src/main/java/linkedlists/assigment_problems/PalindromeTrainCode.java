// The Palindrome Train Code — O(1)-space palindrome check that restores the list.
// Slow/fast pointers find the middle, the second half is reversed in place and compared
// against the first half, then reversed back and reattached — the list reads exactly as
// before on return. Copying values into an array would also be O(n) time but costs O(n)
// extra space; this version keeps O(1) extra space (a few pointers only).
// Complexity: O(n) time over a constant number of passes, O(1) additional space.

class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
    }
}

public class PalindromeTrainCode {
    static Node reverse(Node head) {
        Node prev = null;
        Node cur = head;
        while (cur != null) {
            Node nxt = cur.next;
            cur.next = prev;
            prev = cur;
            cur = nxt;
        }
        return prev;
    }

    static boolean isPalindrome(Node head) {
        if (head == null || head.next == null) {
            return true;
        }
        Node slow = head;
        Node fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        Node second = reverse(slow.next);
        Node p1 = head;
        Node p2 = second;
        boolean ok = true;
        while (p2 != null) {
            if (p1.data != p2.data) {
                ok = false;
                break;
            }
            p1 = p1.next;
            p2 = p2.next;
        }
        slow.next = reverse(second);
        return ok;
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
        System.out.println(isPalindrome(build(1, 2, 3, 2, 1)));
        System.out.println(isPalindrome(build(1, 2, 3, 2)));

        Node code = build(1, 2, 2, 1);
        System.out.println(isPalindrome(code) + " (list afterwards: " + show(code) + ")");
    }
}
