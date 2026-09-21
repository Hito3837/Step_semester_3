import java.util.Scanner;
public class VowelConsonantCounter
{
    public static void countVowelsAndConsonants(String text)
    {
        int vowelCount = 0;
        int consonantCount = 0;
        String vowels = "aeiouAEIOU";
        for (int i = 0; i < text.length(); i++)
        {
            char ch = text.charAt(i);
            if (ch == ' ')
            {
                continue;
            }
            if (vowels.indexOf(ch) != -1)
            {
                vowelCount++;
            }
            else
            {
                consonantCount++;
            }
        }
        System.out.println("Vowels: " + vowelCount + " | Consonants: " + consonantCount);
    }
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter book title: ");
        String bookTitle = sc.nextLine();
        countVowelsAndConsonants(bookTitle);
        sc.close();
    }
}
