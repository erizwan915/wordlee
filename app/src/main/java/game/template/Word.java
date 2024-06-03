package game.template;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.InputStream;

public class Word {
    String word;

    List<String> wordList;
    List<String> possibleGuesses;

    public Word() {
        wordList = new ArrayList<>();
        possibleGuesses = new ArrayList<>();
        createWord();

    }

    public void loadWords(InputStream in, InputStream in2) {
            Scanner scanner = new Scanner(in);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                wordList.add(line);
            }
            scanner.close();

            Scanner scanner2 = new Scanner(in2);
            while (scanner2.hasNextLine()) {
                String line = scanner2.nextLine();
                possibleGuesses.add(line);
            }
            scanner2.close();
    }

    public void createWord() { // get and set a random word from the list
        try{
            loadWords(new FileInputStream("datafiles/wordlist.txt"), new FileInputStream("datafiles/possiblewords.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        int randomID = new Random().nextInt(2310);
        word = wordList.get(randomID);

    }

    public String getWord() { // GETTER
        return word;
    }

    public boolean wordExists(String potentialWord) { // is valid word?
        return possibleGuesses.contains(potentialWord.toLowerCase());
    }

    public static void main(String[] args) {
        Word word = new Word();
        word.createWord();
        System.out.println(word.getWord());
    }
}