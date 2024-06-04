package game.template;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Board {
    private String[][] board;
    private Stack<Move> moveStack;
    public List<String> guessList;
    private final int NUM_GUESSES = 6;
    private List<String> possibleGuesses;

    public Board() {
        board = new String[6][5];
        moveStack = new Stack<>();
        guessList = new ArrayList<>();
        possibleGuesses = new ArrayList<>();
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

    public boolean isLegal(int row, int col, String value) {
        return false;
    }

    public void inputGuess(int row, String guess) {
        if (guess.length() != 5) {
            throw new IllegalArgumentException("Guess must be 5 characters long");
        }
        if (!(wordExists(guess) && guessList.size() < NUM_GUESSES)) {

            throw new IllegalArgumentException("Value " + guess + " not possible cell");
        }

        guessList.add(guess);
        String[] arrOfStr = guess.split("");
        for (int i = 0; i < 5; i++) {
            board[row][i] = arrOfStr[i];
            System.out.println(board[row][i]);
        }

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

    public static void main(String[] args) {
        Board board = new Board();
        board.inputGuess(0, "CRANE");
        board.inputGuess(1, "plane");
    }
}
