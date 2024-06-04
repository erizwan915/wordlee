package game.template;

public class Move {
    int row;
    int col;
    int previousValue;

    Move(int row, int col, int previousValue) {
        this.row = row;
        this.col = col;
        this.previousValue = previousValue;
    }
}