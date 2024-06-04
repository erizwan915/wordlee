package game.template;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class WordleSolver {
    private Word wordGenerator;
    private String correctWord;
    private List<String> attempts;
    private int maxAttempts = 6;

    public WordleSolver() {
        this.wordGenerator = new Word();
        this.correctWord = wordGenerator.getWord();
        this.attempts = new ArrayList<>();
    }

    public void makeAttempt(String attempt) {
        if (attempts.size() < maxAttempts) {
            if (wordGenerator.wordExists(attempt)) {
                attempts.add(attempt);
            } else {
                System.out.println("The word does not exist in the possible guesses list.");
            }
        } else {
            System.out.println("No more attempts allowed.");
        }
    }

    public void giveHint() {
        if (attempts.size() < 3) {
            System.out.println("Possible words to try:");
            List<String> wordList = wordGenerator.wordList;
            List<String> randomWords = getRandomWords(wordList, 10);
            for (String word : randomWords) {
                System.out.println(word);
            }
        } else {
            Set<Character> guessedLetters = new HashSet<>();
            for (String attempt : attempts) {
                for (char c : attempt.toCharArray()) {
                    guessedLetters.add(c);
                }
            }

            for (char c : correctWord.toCharArray()) {
                if (!guessedLetters.contains(c)) {
                    System.out.println("Hint: The word contains the letter '" + c + "'");
                    return;
                }
            }

            System.out.println("No new hints available.");
        }
    }

    private List<String> getRandomWords(List<String> words, int count) {
        List<String> copy = new ArrayList<>(words);
        List<String> randomWords = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < count && !copy.isEmpty(); i++) {
            int randomIndex = rand.nextInt(copy.size());
            randomWords.add(copy.remove(randomIndex));
        }
        return randomWords;
    }

    public boolean isCorrect(String attempt) {
        return attempt.equals(correctWord);
    }

    public static void main(String[] args) {
        WordleSolver solver = new WordleSolver();
        Scanner scanner = new Scanner(System.in);

        while (solver.attempts.size() < solver.maxAttempts) {
            System.out.println("Enter your attempt:");
            String attempt = scanner.nextLine();
            solver.makeAttempt(attempt);

            if (solver.isCorrect(attempt)) {
                System.out.println("Congratulations! You've guessed the correct word.");
                break;
            } else {
                solver.giveHint();
            }

            if (solver.attempts.size() == solver.maxAttempts) {
                System.out.println("Sorry, you've used all attempts. The correct word was: " + solver.correctWord);
            }
        }

        scanner.close();
    }
}
