import java.util.Scanner;
public class WordReversalEncoder
{
    public static String reverseEachWord(String sentence)
    {
        String[] words = sentence.split(" ");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < words.length; i++)
        {
            StringBuilder reversedWord = new StringBuilder();
            String word = words[i];
            for (int j = word.length() - 1; j >= 0; j--)
            {
                reversedWord.append(word.charAt(j));
            }
            result.append(reversedWord);
            if (i < words.length - 1)
            {
                result.append(" ");
            }
        }
        return result.toString();
    }
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter sentence: ");
        String sentence = scanner.nextLine();
        System.out.println(reverseEachWord(sentence));
        scanner.close();
    }
}
