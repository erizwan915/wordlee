package game.template;
import java.util.Scanner;

public class wordlegame {
    
    
        private static final int MAX_ATTEMPTS = 6;
        private static final String TARGET_WORD = "CRANE"; // Target word to guess (should be chosen randomly in a more advanced version)
        private static final int WORD_LENGTH = 5;
    
        public static void main(String[] args) {
            wordlegame game = new wordlegame();
            game.startGame();
        }
    
        private void startGame() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Welcome to Wordle!");
            System.out.println("You have " + MAX_ATTEMPTS + " attempts to guess the 5-letter word.");
    
            for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
                String guess = getUserGuess(scanner, attempt);
    
                if (guess.equals(TARGET_WORD)) {
                    System.out.println("Congratulations! You've guessed the word correctly.");
                    return;
                } else {
                    String feedback = generateFeedback(guess, TARGET_WORD);
                    System.out.println(feedback);
                }
            }
    
            System.out.println("Sorry, you've used all attempts. The correct word was: " + TARGET_WORD);
        }
    
        private String getUserGuess(Scanner scanner, int attempt) {
            while (true) {
                System.out.print("Attempt " + attempt + ": ");
                String guess = scanner.nextLine().toUpperCase();
    
                if (guess.length() == WORD_LENGTH) {
                    return guess;
                } else {
                    System.out.println("Please enter a 5-letter word.");
                }
            }
        }
    
        private String generateFeedback(String guess, String target) {
            char[] feedback = new char[WORD_LENGTH];
            boolean[] guessed = new boolean[WORD_LENGTH];
    
            // Check correct letters at correct positions
            for (int i = 0; i < WORD_LENGTH; i++) {
                if (guess.charAt(i) == target.charAt(i)) {
                    feedback[i] = guess.charAt(i);
                    guessed[i] = true;
                } else {
                    feedback[i] = '-';
                }
            }
    
            // Check correct letters at incorrect positions
            for (int i = 0; i < WORD_LENGTH; i++) {
                if (feedback[i] != '-') continue;
                for (int j = 0; j < WORD_LENGTH; j++) {
                    if (!guessed[j] && guess.charAt(i) == target.charAt(j)) {
                        feedback[i] = guess.charAt(i);
                        guessed[j] = true;
                        break;
                    }
                }
            }
    
            return new String(feedback);
        }
}
    

