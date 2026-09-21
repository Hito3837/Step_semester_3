import java.util.Scanner;
public class LibraryISBNNormalizer
{
    public static String normalizeCode(String raw)
    {
        String trimmed = raw.trim();
        if (trimmed.length() < 3)
        {
            return trimmed;
        }
        String first3 = trimmed.substring(0, 3).toUpperCase();
        String rest = trimmed.substring(3);
        return first3 + rest;
    }
    public static String validateAndFormat(String code)
    {
        if (code.length() != 13)
        {
            return "Invalid: wrong length";
        }
        for (int i = 0; i < 3; i++)
        {
            if (!Character.isLetter(code.charAt(i)))
            {
                return "Invalid: publisher code must be 3 letters";
            }
        }
        for (int i = 3; i < 13; i++)
        {
            if (!Character.isDigit(code.charAt(i)))
            {
                return "Invalid: body must be 10 digits";
            }
        }
        String publisher = code.substring(0, 3);
        String year = code.substring(3, 7);
        String catalog = code.substring(7, 13);
        StringBuilder result = new StringBuilder();
        result.append("[").append(publisher).append("] ");
        result.append("YEAR: ").append(year).append(" | ");
        result.append("CATALOG: ").append(catalog);
        return result.toString();
    }
    public static String processISBN(String raw)
    {
        String normalized = normalizeCode(raw);
        return validateAndFormat(normalized);
    }
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter ISBN code: ");
        String code = scanner.nextLine();
        System.out.println(processISBN(code));
        scanner.close();
    }
}
