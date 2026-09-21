class HostelRoom {
    private final String roomNo;
    private final int beds;
    private int occupied;

    public HostelRoom(String roomNo, int beds) {
        if (roomNo == null || roomNo.trim().isEmpty()) {
            throw new IllegalArgumentException("roomNo cannot be blank");
        }
        if (beds <= 0) {
            throw new IllegalArgumentException("beds must be positive");
        }
        this.roomNo = roomNo.trim();
        this.beds = beds;
        this.occupied = 0;
    }

    boolean allot(String name) {
        if (occupied < beds) {
            occupied++;
            return true;
        }
        return false;
    }

    String getRoomNo() {
        return roomNo;
    }

    int getBeds() {
        return beds;
    }

    int getOccupied() {
        return occupied;
    }

    static HostelRoom findAvailableRoom(HostelRoom[] rooms) {
        for (HostelRoom room : rooms) {
            if (room.occupied < room.beds) {
                return room;
            }
        }
        return null;
    }

    static void safeAllot(HostelRoom[] rooms, String studentName) {
        HostelRoom room = findAvailableRoom(rooms);
        if (room == null) {
            System.out.println("No rooms available for " + studentName);
            return;
        }
        room.allot(studentName);
        System.out.println(studentName + " allotted to room " + room.getRoomNo());
    }

    /* Passing the HostelRoom array does not copy the rooms: Java passes a copy of the
       array *reference*, and each array slot holds a reference to the same HostelRoom
       object the caller owns. findAvailableRoom/safeAllot therefore see (and mutate)
       the caller's original objects, never a copy. */
    public static void main(String[] args) {
        HostelRoom[] rooms = {new HostelRoom("C-214", 3), new HostelRoom("C-507", 2)};
        rooms[0].allot("Alice");
        rooms[0].allot("Bob");
        rooms[1].allot("Carol");
        rooms[1].allot("Dave");
        safeAllot(rooms, "Divya");

        rooms[0].allot("Eve");
        safeAllot(rooms, "Divya");
    }
}