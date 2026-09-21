import java.util.*;
public class UniqueLetterHunt
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        char c = findFirstNonRepeatingChar(input);
        if (c == 0)
        System.out.println("No Non-Repeating Character Found");
        else
        System.out.println("First Non-Repeating Character: '" + c + "'");
    }
    static char findFirstNonRepeatingChar(String text)
    {
        Map<Character, Integer> freq = new LinkedHashMap<>();
        for (char c : text.toCharArray())
        freq.put(c, freq.getOrDefault(c, 0) + 1);
        for (char c : text.toCharArray())
        if (freq.get(c) == 1)
        return c;
        return 0;
    }
}
