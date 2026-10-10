// Library Platform — ID-ordered array, duplicate rejection, capability-gated commands.
// Book IS-A reservable resource, EBook IS-A downloadable one: siblings under LibraryResource
// differ only in their implements clauses, so DOWNLOAD B1 and (symmetrically) RESERVE E1 are
// rejected by the same instanceof gate. The shelf stays sorted by ID, which is what lets FIND
// use binary search (O(log n)) instead of a scan.

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

interface ReservableItem {
    String reserve(String memberId);
}

interface DownloadableItem {
    String download();
}

abstract class LibraryResource {
    private final String id;

    LibraryResource(String id) {
        this.id = id;
    }

    String getId() {
        return id;
    }
}

class Book extends LibraryResource implements ReservableItem {
    Book(String id) {
        super(id);
    }

    @Override
    public String reserve(String memberId) {
        return getId() + " reserved for " + memberId;
    }
}

class EBook extends LibraryResource implements DownloadableItem {
    EBook(String id) {
        super(id);
    }

    @Override
    public String download() {
        return getId() + " downloaded";
    }
}

class LibraryPlatform {
    private final List<LibraryResource> shelf = new ArrayList<>();

    void add(LibraryResource resource) {
        for (LibraryResource existing : shelf) {
            if (existing.getId().equals(resource.getId())) {
                System.out.println("duplicate rejected");
                return;
            }
        }
        shelf.add(resource);
        shelf.sort(Comparator.comparing(LibraryResource::getId));
        System.out.println(resource.getId() + " added");
    }

    private int indexOf(String id) {
        int low = 0;
        int high = shelf.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int cmp = shelf.get(mid).getId().compareTo(id);
            if (cmp == 0) {
                return mid;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    void reserve(String id, String memberId) {
        int index = indexOf(id);
        if (index < 0) {
            System.out.println(id + " not found");
            return;
        }
        LibraryResource resource = shelf.get(index);
        if (resource instanceof ReservableItem) {
            System.out.println(((ReservableItem) resource).reserve(memberId));
        } else {
            System.out.println(id + " rejected: reserve unsupported");
        }
    }

    void download(String id) {
        int index = indexOf(id);
        if (index < 0) {
            System.out.println(id + " not found");
            return;
        }
        LibraryResource resource = shelf.get(index);
        if (resource instanceof DownloadableItem) {
            System.out.println(((DownloadableItem) resource).download());
        } else {
            System.out.println(id + " rejected: download unsupported");
        }
    }

    void find(String id) {
        int index = indexOf(id);
        if (index < 0) {
            System.out.println(id + " not found");
        } else {
            System.out.println(id + " found at index " + index);
        }
    }
}

public class P5_LibraryPlatform {
    public static void main(String[] args) {
        LibraryPlatform library = new LibraryPlatform();

        library.add(new Book("B1"));
        library.add(new Book("B1"));
        library.add(new EBook("E1"));
        library.reserve("B1", "M1");
        library.download("B1");
        library.download("E1");
        library.find("E1");
    }
}
