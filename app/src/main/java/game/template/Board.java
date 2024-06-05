package game.template;
import java.util.Stack;

import javafx.scene.control.Tab;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

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
        try{
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
       /*  if (!(wordExists(guess) && guessList.size() < NUM_GUESSES)) {

            throw new IllegalArgumentException("Value " + guess + " not possible cell");
        }*/

        guessList.add(guess);
        String[] arrOfStr = guess.split("");
        for (int i = 0; i < 5; i++) {
            board[row][i] = arrOfStr[i].toUpperCase();
            System.out.println(board[row][i]);
        }

        return arrOfStr;


        /* 
        // based on other values in the sudoku grid
        int previousValue = board[row][col];
        board[row][col] = value;

        // Push the move onto the stack
        moveStack.push(new Move(row, col, previousValue));
        if (newboard) {
            valuesEntered.add(value);
        }*/
    }

    //check if the guess is correct 
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

    // checks if any of the letters in the guess are in the target word regardless of position
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

    public static void main(String[] args) {
        Board board = new Board();
        String[] guess1 = board.inputGuess(0, "CRANE");
        board.inputGuess(1, "plane");

    }
}
