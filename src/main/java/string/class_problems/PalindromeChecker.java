import java.util.*;
public class PalindromeChecker
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter text: ");
        String text = sc.nextLine();
        System.out.println("Iterative: " + (isPalindromeIterative(text) ? "Palindrome" : "Not Palindrome"));
        System.out.println("Recursive: " + (isPalindromeRecursive(text) ? "Palindrome" : "Not Palindrome"));
        System.out.println("Array Reversal: " + (isPalindromeArrayReversal(text) ? "Palindrome" : "Not Palindrome"));
    }
    static boolean isPalindromeIterative(String text)
    {
        int i = 0, j = text.length() - 1;
        while (i < j)
        {
            if (text.charAt(i) != text.charAt(j)) return false;
            i++; j--;
        }
        return true;
    }
    static boolean isPalindromeRecursive(String text)
    {
        return checkRecursive(text, 0, text.length() - 1);
    }
    static boolean checkRecursive(String text, int i, int j)
    {
        if (i >= j)
        return true;
        if (text.charAt(i) != text.charAt(j))
        return false;
        return checkRecursive(text, i + 1, j - 1);
    }
    static boolean isPalindromeArrayReversal(String text)
    {
        char[] chars = text.toCharArray();
        char[] reversed = new char[chars.length];
        for (int i = 0; i < chars.length; i++)
        {
            reversed[i] = chars[chars.length - 1 - i];
        }
        return java.util.Arrays.equals(chars, reversed);
    }
}
