import java.util.Scanner;
public class BookIssue
{
    private String title;
    private String borrowerName;
    private int daysOverdue;
    public BookIssue(String title, String borrowerName, int daysOverdue)
    {
        this.title = title;
        this.borrowerName = borrowerName;
        this.daysOverdue = daysOverdue;
    }
    public double fineAmount()
    {
        if (daysOverdue > 0)
        {
            return daysOverdue * 5.0;
        }
        return 0.0;
    }
    public boolean isSeverelyOverdue()
    {
        return daysOverdue > 14;
    }
    public static double totalFineCollected(BookIssue[] issues)
    {
        double total = 0.0;
        for (BookIssue issue : issues)
        {
            total += issue.fineAmount();
        }
        return total;
    }
    @Override
    public String toString()
    {
        return title + " - " + daysOverdue + " days";
    }
    public String getTitle()
    {
        return title;
    }
    public String getBorrowerName()
    {
        return borrowerName;
    }
    public int getDaysOverdue()
    {
        return daysOverdue;
    }
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        BookIssue[] books = new BookIssue[5];
        System.out.println("Enter details for 5 books:");
        for (int i = 0; i < 5; i++)
        {
            System.out.print("Book " + (i + 1) + " title: ");
            String title = sc.nextLine();
            System.out.print("Borrower name: ");
            String borrowerName = sc.nextLine();
            System.out.print("Days overdue: ");
            int daysOverdue = sc.nextInt();
            sc.nextLine();
            books[i] = new BookIssue(title, borrowerName, daysOverdue);
        }
        System.out.println("\nBook Overdue Status:");
        for (BookIssue book : books)
        {
            System.out.println(book.getTitle() + " - " + book.getDaysOverdue() + " days" + (book.isSeverelyOverdue() ? " - Severely overdue" : " - OK"));
        }
        System.out.println("\nFine Details:");
        for (BookIssue book : books)
        {
            System.out.println(book.getTitle() + " - " + book.getDaysOverdue() + " days - " + (book.fineAmount() > 0 ? "Rs " + book.fineAmount() : "OK"));
        }
        System.out.println("\nTotal fine collected: Rs " + totalFineCollected(books));
        sc.close();
    }
}
