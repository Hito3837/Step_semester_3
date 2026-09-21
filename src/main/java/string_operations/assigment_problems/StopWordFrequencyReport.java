import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
public class StopWordFrequencyReport
{
    public static void printFilteredWordFrequency(String feedback)
    {
        String[] stopWords = {"the", "was", "and", "a", "is", "of", "in"};
        String cleaned = feedback.replace(",", "").replace(".", ""); cleaned = cleaned.toLowerCase();
        String[] words = cleaned.split("\\s+");
        HashMap<String, Integer> frequency = new HashMap<>();
        for (String word : words)
        {
            boolean isStopWord = false;
            for (String stopWord : stopWords)
            {
                if (word.equals(stopWord))
                {
                    isStopWord = true;
                    break;
                }
            }
            if (!isStopWord && !word.isEmpty())
            {
                frequency.put(word, frequency.getOrDefault(word, 0) + 1);
            }
        }
        ArrayList<String> sortedWords = new ArrayList<>(frequency.keySet());
        Collections.sort(sortedWords, (w1, w2) -> frequency.get(w2).compareTo(frequency.get(w1)));
        for (String word : sortedWords)
        {
            System.out.println(word + ": " + frequency.get(word));
        }
    }
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter feedback text: ");
        String feedback = scanner.nextLine();
        printFilteredWordFrequency(feedback);
        scanner.close();
    }
}
