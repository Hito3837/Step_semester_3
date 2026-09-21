import java.util.Random;
import java.util.Scanner;
public class RockPaperScissors
{
    private static final int TOTAL_ROUNDS = 5;
    private static final String[] MOVES = {"Rock", "Paper", "Scissors"};
    public static String playRound(String playerMove, String computerMove)
    {
        String p = normalizeMove(playerMove);
        String c = normalizeMove(computerMove);
        if (p.equals(c))
        {
            return "Draw";
        }
        if ((p.equals("Rock") && c.equals("Scissors")) || (p.equals("Paper") && c.equals("Rock")) || (p.equals("Scissors") && c.equals("Paper")))
        {
            return "Player Wins";
        }
        return "Computer Wins";
    }
    private static String normalizeMove(String move)
    {
        if (move == null)
        {
            return "";
        }
        String normalized = move.trim().toLowerCase();
        switch (normalized)
        {
            case "r":
            case "rock":
                return "Rock";
            case "p":
            case "paper":
                return "Paper";
            case "s":
            case "scissors":
                return "Scissors";
            default:
                return "";
        }
    }
    private static String getComputerMove(Random random)
    {
        return MOVES[random.nextInt(MOVES.length)];
    }
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int wins = 0;
        int losses = 0;
        int draws = 0;
        System.out.println("Welcome to the College Coding Arcade!");
        System.out.println("Rock-Paper-Scissors match against the computer\n");
        String[][] roundTable = new String[TOTAL_ROUNDS][4];
        for (int round = 1; round <= TOTAL_ROUNDS; round++)
        {
            System.out.print("Round " + round + " - Enter your move (Rock/Paper/Scissors): ");
            String playerMove = scanner.nextLine();
            while (normalizeMove(playerMove).isEmpty())
            {
                System.out.print("Invalid move. Please enter Rock, Paper, or Scissors: ");
                playerMove = scanner.nextLine();
            }
            String normalizedPlayerMove = normalizeMove(playerMove);
            String computerMove = getComputerMove(random);
            String result = playRound(normalizedPlayerMove, computerMove);
            switch (result)
            {
                case "Player Wins":
                    wins++;
                    break;
                case "Computer Wins":
                    losses++;
                    break;
                default:
                    draws++;
                    break;
            }
            roundTable[round - 1][0] = String.valueOf(round);
            roundTable[round - 1][1] = normalizedPlayerMove;
            roundTable[round - 1][2] = computerMove;
            roundTable[round - 1][3] = result;
            System.out.println("Player: " + normalizedPlayerMove + " | Computer: " + computerMove + " | " + result);
            System.out.println();
        }
        System.out.println("Final Summary Table");
        System.out.printf("%-6s %-12s %-15s %-12s%n", "Round", "Player", "Computer", "Result");
        System.out.println("-----------------------------------------------");
        for (String[] row : roundTable)
        {
            System.out.printf("%-6s %-12s %-15s %-12s%n", row[0], row[1], row[2], row[3]);
        }
        double winPercentage = (double) wins / TOTAL_ROUNDS * 100;
        System.out.println();
        System.out.printf("Wins: %d | Losses: %d | Draws: %d | Win %% = %.1f%%%n",
                wins, losses, draws, winPercentage);
        scanner.close();
    }
}
