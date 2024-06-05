package game.template;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.stream.Collectors;

public class Board {
    private String[][] board;
    private Stack<Move> moveStack;
    public List<String> guessList;
    private final int NUM_GUESSES = 6;
    private List<String> possibleGuesses;
    private String word;
    private List<String> wordyList;

    public Board() {
        board = new String[6][5];
        moveStack = new Stack<>();
        guessList = new ArrayList<>();
        possibleGuesses = new ArrayList<>();
        wordyList = new ArrayList<>();
    }

    public String createWord(InputStream in) {
        Scanner scanner = new Scanner(in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            wordyList.add(line);
        }
        scanner.close();

        int randomID = new Random().nextInt(wordyList.size());
        word = wordyList.get(randomID);
        return word;
    }

    public void loadWords(InputStream in) {
        Scanner scanner2 = new Scanner(in);
        while (scanner2.hasNextLine()) {
            String line = scanner2.nextLine();
            possibleGuesses.add(line);
        }
        scanner2.close();
    }

    public boolean wordExists(String potentialWord) {
        try {
            loadWords(new FileInputStream("datafiles/possiblewords.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        return possibleGuesses.contains(potentialWord.toLowerCase());
    }

    public boolean wordExists(InputStream in, String potentialWord) {
        Scanner scanner2 = new Scanner(in);
        while (scanner2.hasNextLine()) {
            String line = scanner2.nextLine();
            possibleGuesses.add(line);
        }
        scanner2.close();
        return possibleGuesses.contains(potentialWord.toLowerCase());
    }

    public boolean isLegal(int row, int col, String value) {
        return false;
    }

    public String[] inputGuess(int row, String guess) {
        if (guess.length() != 5) {
            throw new IllegalArgumentException("Guess must be 5 characters long");
        }
        
        if (!wordExists(guess)) {
            throw new IllegalArgumentException("The word does not exist in the possible guesses list.");
        }
        if (guessList.size() >= NUM_GUESSES) {
            throw new IllegalArgumentException("No more attempts allowed.");
        }
        else {
            guessList.add(guess);
            String[] arrOfStr = guess.split("");
            for (int i = 0; i < 5; i++) {
                board[row][i] = arrOfStr[i].toUpperCase();
                System.out.println(board[row][i]);
            }
            return arrOfStr;
        }
        
    }

    

    // Check if the guess is correct
    public boolean[] isCorrect(String[] guess, String target) {
        boolean[] correct = new boolean[]{false, false, false, false, false};
        String[] targetArr = target.split("");
        for (int i = 0; i < 5; i++) {
            if (guess[i].equals(targetArr[i].toLowerCase())) {
                correct[i] = true;
            }
        }
        return correct;
    }

    // Checks if any of the letters in the guess are in the target word regardless of position
    public boolean[] isCorrectButWrongSpot(String[] guess, String target) {
        boolean[] correct = new boolean[]{false, false, false, false, false};
        target = target.toLowerCase();
        String[] targetArr = target.split("");
        for (int i = 0; i < 5; i++) {
            if (target.contains(guess[i]) && !guess[i].equals(targetArr[i].toLowerCase())) {
                correct[i] = true;
            }
        }
        return correct;
    }

    public Board getBoard() {
        return this;
    }


    public List<String> displayInitialHint() {
        if (guessList.size() > 0) {
            throw new IllegalArgumentException("You have already used the initial hint available.");
        }
        else {
            return getRandomWords(wordyList, 10);
        }
        
        
    }

    public List<String> displayHintWithGuesses() {
        
            System.out.println("Possible words to try:");
            List<String> possibleWords = wordyList.stream()
                    .filter(word -> !guessList.contains(word))
                    .collect(Collectors.toList());
            List<String> randomWords = getRandomWords(possibleWords, 10);
            return randomWords;
        
       
    }

    public String displayLetterHint() {
        if (guessList.size() <3) {
            throw new IllegalArgumentException("You have not made any guesses yet.");
        }
        else{
            Set<Character> guessedLetters = new HashSet<>();
            for (String attempt : guessList) {
                for (char c : attempt.toCharArray()) {
                    guessedLetters.add(c);
                }
            }
    
            for (char c : word.toCharArray()) {
                if (!guessedLetters.contains(c)) {
                    String message = "The letter "+c+" is in the word.";
                    return message;
                }
            }
        }
        return null;
              
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
        return attempt.equals(word);
    }

    public void printAttempts() {
        System.out.println("Guesses made so far: " + guessList);
    }

    
    public static void main(String[] args) {
        Board board = new Board();
        String[] guess1 = board.inputGuess(0, "CRANE");
        board.inputGuess(1, "plane");

    }
}
