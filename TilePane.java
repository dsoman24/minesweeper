import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TilePane extends StackPane {
    private int row;
    private int column;
    private Minesweeper minesweeper;
    private final Rectangle background = new Rectangle(30, 30, Color.DARKGRAY);
    private final Rectangle foreground = new Rectangle(25, 25, Color.LIGHTGRAY);

    public TilePane(int row, int column, MinesweeperPane minesweeperPane) {

        this.row = row;
        this.column = column;
        this.minesweeper = minesweeperPane.getMinesweeper();

        getChildren().add(background);
            getChildren().add(foreground);

            setOnMouseClicked(e -> {
                if (minesweeper.isPlaying()) {
                    if (e.getButton() == MouseButton.SECONDARY) {
                        minesweeperPane.flag(this.row, this.column);
                    } else {
                        minesweeperPane.clear(this.row, this.column);
                    }
                }
            });
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
            if (currentTile.hasMine()) {
                foreground.setFill(Color.DARKGOLDENROD);
                getChildren().add(new Label("M"));
            } else {
                foreground.setFill(Color.CORNFLOWERBLUE);
                getChildren().add(
                    new Label(String.format("%s", currentTile.getNumNeighboringMines() == 0 ? " " : currentTile.getNumNeighboringMines()))
                );
            }
        }
    }

    public void reveal() {
        Tile currentTile = getCorrespondingTile();
        if (currentTile.hasMine() && !currentTile.isFlagged()) {
            foreground.setFill(Color.GOLDENROD);
            getChildren().add(new Label("M"));
        } else if (!currentTile.hasMine() && currentTile.isFlagged()) {
            getChildren().add(new Label("X"));
        }
    }

    public Tile getCorrespondingTile() {
        return minesweeper.getTiles()[row][column];
    }
}
