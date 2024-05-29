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
    List<String> wordsPossible;

    public Word() {
        wordList = new ArrayList<>();
        wordsPossible = new ArrayList<>();

    }

    public void loadWords(InputStream in) {
            Scanner scanner = new Scanner(in);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                wordList.add(line);
            }
            scanner.close();
    }

    public void createWord() { // get and set a random word from the list
        try{
            loadWords(new FileInputStream("datafiles/wordlist.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        int randomID = new Random().nextInt(2310);
        word = wordList.get(randomID);
        System.out.println(word);

    }

    public String getWord() { // GETTER
        return word;
    }

    public boolean wordExists(String potentialWord) { // is valid word?
        return wordsPossible.contains(potentialWord.toLowerCase());
    }

    public static void main(String[] args) {
        Word word = new Word();
        word.createWord();
    }
}