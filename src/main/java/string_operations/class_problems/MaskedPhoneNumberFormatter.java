import java.util.Scanner;
public class MaskedPhoneNumberFormatter
{
    public static String maskPhoneNumber(String phone)
    {
        if (phone == null || phone.length() != 10)
        {
            return "Invalid phone number";
        }
        for (int i = 0; i < phone.length(); i++)
        {
            if (!Character.isDigit(phone.charAt(i)))
            {
                return "Invalid phone number";
            }
        }
        StringBuilder masked = new StringBuilder();
        masked.append("XXXXXX");
        masked.append("-");
        masked.append(phone.substring(6));
        return masked.toString();
    }
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter phone number (10 digits): ");
        String phone = sc.nextLine();
        System.out.println("Output: " + maskPhoneNumber(phone));
        sc.close();
    }
}
