// Library Catalog Lookup (library-method variant) — same task as P3, solved with
// Arrays.binarySearch plus an ISBN Comparator instead of a hand-rolled loop.
// The PDF lists this task twice (Problems 3 and 4); this file shows the standard-library
// route to the identical result. Sorted order is still what makes it logarithmic.
// Complexity: O(log n) time per query (comparisons on sorted data), O(1) extra space.

import java.util.Arrays;
import java.util.Comparator;

class BookRecord {
    final String isbn;
    final String title;

    BookRecord(String isbn, String title) {
        this.isbn = isbn;
        this.title = title;
    }
}

public class P4_LibraryLookupBinarySearch {
    private static final Comparator<BookRecord> BY_ISBN =
            Comparator.comparing(record -> record.isbn);

    static String findBook(BookRecord[] catalog, String targetIsbn) {
        int index = Arrays.binarySearch(catalog, new BookRecord(targetIsbn, ""), BY_ISBN);
        if (index >= 0) {
            return catalog[index].title;
        }
        return "Not Found";
    }

    public static void main(String[] args) {
        BookRecord[] catalog = {
            new BookRecord("0001112223", "Introduction to Algebra"),
            new BookRecord("0002223334", "Beginning Python"),
            new BookRecord("0003334445", "Classic Mythology"),
            new BookRecord("0004445556", "Data and Society"),
            new BookRecord("0005556667", "European History")
        };
        System.out.println(findBook(catalog, "0003334445"));
        System.out.println(findBook(catalog, "0009998887"));
    }
}
