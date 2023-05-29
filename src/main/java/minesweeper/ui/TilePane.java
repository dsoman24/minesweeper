package src.main.java.minesweeper.ui;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import src.main.java.minesweeper.logic.Minesweeper;
import src.main.java.minesweeper.logic.Tile;

public class TilePane extends StackPane {
    private int row;
    private int column;
    private Minesweeper minesweeper;
    private MinesweeperPane minesweeperPane;
    private final Rectangle background = new Rectangle(30, 30, Color.DARKGRAY);
    private final Rectangle foreground = new Rectangle(25, 25, Color.LIGHTGRAY);
    private final Rectangle decisionLayer = new Rectangle(25, 25, new Color(0, 0, 0, 0));
    private Label informationLabel;

    private final Color[] numberLabelColors = new Color[]{
        Color.BLUE, Color.GREEN, Color.RED, Color.PURPLE, Color.MAROON, Color.TEAL, Color.BLACK, Color.GRAY
    };

    public TilePane(int row, int column, MinesweeperPane minesweeperPane) {

        this.row = row;
        this.column = column;
        this.minesweeperPane = minesweeperPane;
        this.minesweeper = minesweeperPane.getMinesweeper();
        informationLabel = new Label();

        getChildren().add(background);
        getChildren().add(foreground);
        getChildren().add(decisionLayer);
        getChildren().add(informationLabel);
        informationLabel.setFont(Font.font(9));

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
            if (!minesweeper.hasMine(currentTile)) {
                foreground.setFill(Color.WHITE);
                int number = currentTile.getNumberOfNeighboringMines();
                informationLabel.setText(String.format("%s", number == 0 ? " " : number));
                if (number > 0) {
                    informationLabel.setTextFill(numberLabelColors[number - 1]);
                }
            }
        }
    }

    public void reveal(Tile badTile) {
        Tile currentTile = getCorrespondingTile();
        if (minesweeper.hasMine(currentTile) && !currentTile.isFlagged()) {
            foreground.setFill(Color.YELLOW);
            informationLabel.setText("M");
            if (row == badTile.getRow() && column == badTile.getColumn()) {
                foreground.setFill(Color.ORANGE);
            }
        } else if (!minesweeper.hasMine(currentTile) && currentTile.isFlagged()) {
            informationLabel.setText("X");
        }
    }

    public Tile getCorrespondingTile() {
        return minesweeper.getTileAt(row, column);
    }

    public void addNumericalOverlay(Number value) {
        double doubleValue = (double) value;
        informationLabel.setText(String.format("%.2f", doubleValue));

        Color color = calculateGradientColor(doubleValue);
        decisionLayer.setFill(color);
    }

    public void removeNumericalOverlay() {
        informationLabel.setText("");
        decisionLayer.setFill(new Color(0, 0, 0, 0));
    }

    private Color calculateGradientColor(double value) {
        Color greenColor = Color.GREEN;
        Color redColor = Color.RED;

        double red = redColor.getRed() * value + greenColor.getRed() * (1 - value);
        double green = redColor.getGreen() * value + greenColor.getGreen() * (1 - value);
        double blue = redColor.getBlue() * value + greenColor.getBlue() * (1 - value);

        return new Color(red, green, blue, 1.0);
    }
}
