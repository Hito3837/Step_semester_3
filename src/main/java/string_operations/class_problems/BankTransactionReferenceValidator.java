import java.util.Scanner;
public class BankTransactionReferenceValidator
{
    public static String normalizeReference(String raw)
    {
        String trimmed = raw.trim();
        if (trimmed.length() < 3)
        {
            return trimmed;
        }
        String bankCode = trimmed.substring(0, 3).toUpperCase();
        String remainder = trimmed.substring(3);
        return bankCode + remainder;
    }
    public static String validateAndFormat(String reference)
    {
        if (reference.length() != 14)
        {
            return "Invalid: wrong length";
        }
        for (int i = 0; i < 3; i++)
        {
            if (!Character.isLetter(reference.charAt(i)))
            {
                return "Invalid: bank code must be 3 letters";
            }
        }
        for (int i = 3; i < 14; i++)
        {
            if (!Character.isDigit(reference.charAt(i)))
            {
                return "Invalid: non-digit body";
            }
        }
        String bankCode = reference.substring(0, 3);
        String dateStr = reference.substring(3, 9);
        String seqStr = reference.substring(9, 14);
        String formattedDate = dateStr.substring(0, 2) + "/" + dateStr.substring(2, 4) + "/" + dateStr.substring(4, 6);
        StringBuilder result = new StringBuilder();
        result.append("[").append(bankCode).append("] ");
        result.append("DATE: ").append(formattedDate).append(" | ");
        result.append("SEQ: ").append(seqStr);
        return result.toString();
    }
    public static String processReference(String raw)
    {
        String normalized = normalizeReference(raw);
        return validateAndFormat(normalized);
    }
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter transaction reference: ");
        String reference = sc.nextLine();
        System.out.println("Output: " + processReference(reference));
        sc.close();
    }
}
