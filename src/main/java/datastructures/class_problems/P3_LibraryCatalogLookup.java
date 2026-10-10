// Library Catalog Lookup — hand-rolled binary search on ISBN order.
// Because the catalog is sorted by ISBN and queries vastly outnumber updates, each lookup
// halves the search space per comparison instead of scanning. ISBNs compare as strings
// (equal-length numeric strings order the same lexicographically and numerically).
// Complexity: O(log n) time per query, O(1) extra space. The one-time sort cost is paid once
// while every one of the many queries stays logarithmic — ideal for read-heavy catalogs.

class BookEntry {
    final String isbn;
    final String title;

    BookEntry(String isbn, String title) {
        this.isbn = isbn;
        this.title = title;
    }
}

public class P3_LibraryCatalogLookup {
    static String findBook(BookEntry[] catalog, String targetIsbn) {
        int low = 0;
        int high = catalog.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int cmp = catalog[mid].isbn.compareTo(targetIsbn);
            if (cmp == 0) {
                return catalog[mid].title;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return "Not Found";
    }

    public static void main(String[] args) {
        BookEntry[] catalog = {
            new BookEntry("0001112223", "Introduction to Algebra"),
            new BookEntry("0002223334", "Beginning Python"),
            new BookEntry("0003334445", "Classic Mythology"),
            new BookEntry("0004445556", "Data and Society"),
            new BookEntry("0005556667", "European History")
        };
        System.out.println(findBook(catalog, "0003334445"));
        System.out.println(findBook(catalog, "0009998887"));
    }
}
