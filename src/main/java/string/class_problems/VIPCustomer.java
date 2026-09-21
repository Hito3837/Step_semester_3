import java.util.*;
public class VIPCustomer
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        String customerName = sc.nextLine();
        System.out.println("Original Name: " + customerName);
        System.out.println("Reversed Name: " + reverseCustomerName(customerName));
    }
    static String reverseCustomerName(String customerName)
    {
        char[] characters = customerName.toCharArray();
        StringBuilder reversedName = new StringBuilder();
        for (int i = characters.length - 1; i >= 0; i--)
        {
            reversedName.append(characters[i]);
        }
        return reversedName.toString();
    }
}
