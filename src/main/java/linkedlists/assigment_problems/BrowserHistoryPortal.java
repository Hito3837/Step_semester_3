// Campus Portal Browser History — doubly linked list with a current-page cursor.
// visit() links a fresh node after the cursor and drops the whole forward chain in O(1) (the
// orphaned nodes become unreachable and are reclaimed by the garbage collector). back() and
// forward() walk at most `steps` links, stopping at the first/last page available.
// Complexity: visit O(1); back/forward O(steps); space O(number of live history nodes).

class Node {
    String url;
    Node prev;
    Node next;

    Node(String url) {
        this.url = url;
    }
}

class BrowserHistory {
    private Node current;

    BrowserHistory(String home) {
        current = new Node(home);
    }

    void visit(String url) {
        Node page = new Node(url);
        page.prev = current;
        current.next = page; // everything that followed is dropped here
        current = page;
    }

    String back(int steps) {
        while (steps > 0 && current.prev != null) {
            current = current.prev;
            steps--;
        }
        return current.url;
    }

    String forward(int steps) {
        while (steps > 0 && current.next != null) {
            current = current.next;
            steps--;
        }
        return current.url;
    }
}

public class BrowserHistoryPortal {
    public static void main(String[] args) {
        BrowserHistory browser = new BrowserHistory("srm.edu");
        browser.visit("a.com");
        browser.visit("b.com");
        browser.visit("c.com");

        String[] stops = new String[6];
        stops[0] = browser.back(1);
        stops[1] = browser.back(1);
        stops[2] = browser.forward(1);
        browser.visit("d.com");
        stops[3] = browser.forward(2);
        stops[4] = browser.back(2);
        stops[5] = browser.back(7);

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < stops.length; i++) {
            if (i > 0) {
                out.append(", ");
            }
            out.append(stops[i]);
        }
        System.out.println(out);
    }
}
