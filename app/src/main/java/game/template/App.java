/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package game.template;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import java.util.List;
import java.util.ArrayList;

public class App extends Application
{
    private static final int NUM_ROWS = 7;
    private static final int NUM_COLS = 5;
    private VBox root;
    private TextField[][] textFields = new TextField[NUM_ROWS+1][NUM_COLS];
    private Label[][] labels = new Label[NUM_ROWS+1][NUM_COLS];
    private Board board = new Board();
    private int guessCount = 0;
    private File wordlist = new File("../datafiles/wordlist.txt");
    private final String TARGET_WORD;
    private List<String> startlist;
    private List<String> moveList = new ArrayList<>();
    //private List<String> possiblelist;
    public App() throws FileNotFoundException {
        TARGET_WORD = board.createWord(new FileInputStream(wordlist)).toUpperCase();
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        root = new VBox();

        root.getChildren().add(createMenuBar());

        GridPane gridPane = new GridPane();
        root.getChildren().add(gridPane);
        gridPane.getStyleClass().add("grid-pane");
        
        TextField wordguess = new TextField();
        gridPane.add(wordguess, 0, 0, 5, 1);

        for (int row = 1; row < NUM_ROWS; row++)
        {
            for (int col = 0; col < NUM_COLS; col++)
            {
                labels[row][col] = new Label("");
                Label label = labels[row][col];
                
                // 6 rows, 5 columns for WORDLE
                label.setId(row + "-" + col);
                gridPane.add(label, col, row);
            }
        }

        

        root.setOnKeyPressed(event -> {
            System.out.println("Key pressed: " + event.getCode());
            switch (event.getCode())
            {
                // check for the key input
                case ESCAPE:
                    // remove focus from the textfields by giving it to the root VBox
                    root.requestFocus();
                    System.out.println("You pressed ESC key");
                    break;
                case ENTER:

                    //FileChooser fileChooser = new FileChooser();
                    //fileChooser.setInitialDirectory(new File("../datafiles/possiblewords.txt"));
                    File wordFile = new File("../datafiles/possiblewords.txt");
                    //fileChooser.showOpenDialog(primaryStage);
                    boolean wordExists = false;
                    if (wordFile != null) {
                        try {
                            wordExists = board.wordExists(new FileInputStream(wordFile), wordguess.getText());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if(wordguess.getText().length() != 5) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Invalid Guess");
                        alert.setHeaderText(null);
                        alert.setContentText("Guess must be 5 characters long");
                        alert.showAndWait();
                        throw new IllegalArgumentException("Guess must be 5 characters long");
                        

                    }
                    if(!(wordExists && guessCount <= 6)) {
                        throw new IllegalArgumentException("Value " + wordguess.getText() + " not possible cell");
                    }
                    String[] wordGuessed = board.inputGuess(guessCount, wordguess.getText());
                    guessCount++;


                    if (wordguess.getText().toLowerCase().equals(TARGET_WORD.toLowerCase())) {
                        // Create an alert that says you've guessed the word
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Congratulations!");
                        alert.setHeaderText(null);
                        alert.setContentText("You've guessed the word correctly!");
                        alert.showAndWait();
                    }

                    // clear the textfield
                    wordguess.clear();
                  
                    for (int col = 0; col < NUM_COLS; col++)
                    {    
                        labels[guessCount][col].setText(wordGuessed[col].toUpperCase());

                    }

                    boolean[] greenSpots = board.isCorrect(wordGuessed, TARGET_WORD);
                    boolean[] yellowSpots = board.isCorrectButWrongSpot(wordGuessed, TARGET_WORD);
                    
                    for (int col = 0; col < NUM_COLS; col++) {
                        System.out.println(wordGuessed[col]);
                        System.out.println(greenSpots[col]);
                        System.out.println(yellowSpots[col]);
                        
                    }

                    for (int col = 0; col < NUM_COLS; col++) {
                        if (greenSpots[col]) {
                            labels[guessCount][col].getStyleClass().add("correct-label");
                        } else if (yellowSpots[col]) {
                            labels[guessCount][col].getStyleClass().add("correct-wrong-spot-label");
                        }
                        else{
                            labels[guessCount][col].getStyleClass().add("incorrect-label");
                        }
                    }

                    System.out.println(TARGET_WORD);

                    if (guessCount == 6) {
                        // Create an alert that says you've ran out of guesses
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Out of Guesses");
                        alert.setHeaderText(null);
                        alert.setContentText("You've ran out of guesses! The word was: " + TARGET_WORD);
                        alert.showAndWait();
                    }

                    
                    System.out.println("You pressed ENTER key");
                    break;
                default:
                    System.out.println("you typed key: " + event.getCode());
                    break;
                
            }
        });

        // don't give a width or height to the scene
        Scene scene = new Scene(root);

        URL styleURL = getClass().getResource("/style.css");
        String stylesheet = styleURL.toExternalForm();
        scene.getStylesheets().add(stylesheet);
        primaryStage.setTitle("WORDLE");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            System.out.println("oncloserequest");
        });

    }

    private MenuBar createMenuBar()
    {
        MenuBar menuBar = new MenuBar();
    	menuBar.getStyleClass().add("menubar");

        //
        // File Menu
        //
    	Menu fileMenu = new Menu("File");

        addMenuItem(fileMenu, "Load from file", () -> {
            System.out.println("Load from file");
        });

        addMenuItem(fileMenu, "Undo", () -> {
            System.out.println("Undo move");
            for(int col = 0; col < NUM_COLS; col++){
                moveList.add(labels[guessCount][col].getText());
                labels[guessCount][col].getStyleClass().remove("incorrect-label");
                labels[guessCount][col].getStyleClass().remove("correct-label");
                labels[guessCount][col].getStyleClass().remove("correct-wrong-spot-label");
                labels[guessCount][col].setText("");
            }
            guessCount--;
        });

        addMenuItem(fileMenu, "Redo last move", () -> {
            System.out.println("Redo Last move");
            guessCount++;
            for(int col = 0; col < NUM_COLS; col++){
                labels[guessCount][col].setText(moveList.get(col));
            }
        });

        menuBar.getMenus().add(fileMenu);

        Menu HintMenu = new Menu("Hint");
        addMenuItem(HintMenu, "Get Me Started!", () -> {
            
            try {                
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Some words to get you started!");
                alert.setHeaderText(null);
                alert.setContentText(String.join("\n", board.displayInitialHint()));
                alert.showAndWait();

            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Initial Hint Error");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
            
        });

        addMenuItem(HintMenu, "Give Me a Possible List of Values", () -> {
             
            try {                
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Here's some more words!");
                alert.setHeaderText(null);
                alert.setContentText(String.join("\n", board.displayHintWithGuesses()));
                alert.showAndWait();

            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hint Error");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
            
        });

        addMenuItem(HintMenu, "Give Me a Letter Hint!", () -> {

            try {                
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Here's some more words!");
                alert.setHeaderText(null);
                alert.setContentText(board.displayLetterHint());
                alert.showAndWait();

            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hint Error");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
            
        });
        menuBar.getMenus().add(HintMenu);


        return menuBar;
    }

    private void addMenuItem(Menu menu, String name, Runnable action)
    {
        MenuItem menuItem = new MenuItem(name);
        menuItem.setOnAction(event -> action.run());
        menu.getItems().add(menuItem);
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
}