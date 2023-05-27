package src.main.java.minesweeper.ui;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import src.main.java.minesweeper.logic.Minesweeper;
import src.main.java.minesweeper.logic.Tile;

public class TilePane extends StackPane {
    private int row;
    private int column;
    private Minesweeper minesweeper;
    private MinesweeperPane minesweeperPane;
    private final Rectangle background = new Rectangle(30, 30, Color.DARKGRAY);
    private final Rectangle foreground = new Rectangle(25, 25, Color.LIGHTGRAY);

    private final Color[] numberLabelColors = new Color[]{
        Color.BLUE, Color.GREEN, Color.RED, Color.PURPLE, Color.MAROON, Color.TEAL, Color.BLACK, Color.GRAY
    };

    public TilePane(int row, int column, MinesweeperPane minesweeperPane) {

        this.row = row;
        this.column = column;
        this.minesweeperPane = minesweeperPane;
        this.minesweeper = minesweeperPane.getMinesweeper();

        getChildren().add(background);
            getChildren().add(foreground);

            setOnMouseClicked(e -> {
                trigger(e);
            });
    }

    /**
     * Method to trigger a tile.
     * Secondary mouse button to flag
     * Primary mouse button to clear
     */
    public void trigger(MouseEvent e) {
        if (minesweeper.isPlaying()) {
            if (e.getButton() == MouseButton.SECONDARY) {
                minesweeperPane.flag(this.row, this.column);
            } else {
                minesweeperPane.clear(this.row, this.column);
            }
        }
    }

    public void update() {
        Tile currentTile = getCorrespondingTile();
        if (!currentTile.isCleared()) {
            if (currentTile.isFlagged()) {
                foreground.setFill(Color.LIGHTCORAL);
            } else {
                foreground.setFill(Color.LIGHTGRAY);
            }
        } else {
            if (minesweeper.hasMine(currentTile)) {
                foreground.setFill(Color.DARKGOLDENROD);
                getChildren().add(new Label("M"));
            } else {
                foreground.setFill(Color.CORNFLOWERBLUE);
                int number = currentTile.getNumberOfNeighboringMines();
                Label minesLabel = new Label(String.format("%s", number == 0 ? " " : number));
                if (number > 0) {
                    minesLabel.setTextFill(numberLabelColors[number - 1]);
                }
                getChildren().add(minesLabel);
            }
        }
    }

    public void reveal(Tile badTile) {
        Tile currentTile = getCorrespondingTile();
        if (minesweeper.hasMine(currentTile) && !currentTile.isFlagged()) {
            foreground.setFill(Color.YELLOW);
            getChildren().add(new Label("M"));
            if (row == badTile.getRow() && column == badTile.getColumn()) {
                foreground.setFill(Color.ORANGE);
            }
        } else if (!minesweeper.hasMine(currentTile) && currentTile.isFlagged()) {
            getChildren().add(new Label("X"));
        }
    }

    public Tile getCorrespondingTile() {
        return minesweeper.getTileAt(row, column);
    }
}
