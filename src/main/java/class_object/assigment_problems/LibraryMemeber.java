import java.util.Scanner;
public class LibraryMember
{
    private String name;
    private String memberId;
    private int booksIssued;
    private static String libraryName = "City Library";
    private static int memberCount = 0;
    public LibraryMember(String name)
    {
        this.name = name;
        memberCount++;
        this.memberId = "LM-" + String.format("%03d", memberCount);
        this.booksIssued = 0;
    }
    public void printMemberCard()
    {
        System.out.println(name + " | " + memberId);
    }
    public static void printTotalMembers()
    {
        System.out.println("Total members: " + memberCount);
    }
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter member 1 name: ");
        LibraryMember member1 = new LibraryMember(sc.nextLine());
        System.out.print("Enter member 2 name: ");
        LibraryMember member2 = new LibraryMember(sc.nextLine());
        member1.printMemberCard();
        member2.printMemberCard();
        LibraryMember.printTotalMembers();
        sc.close();
    }
}
