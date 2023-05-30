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

    // dimensions
    private static final int BACKGROUND_WIDTH = 30;
    private static final int BORDER_WIDTH = 2;
    private static final int FOREGROUND_WIDTH = BACKGROUND_WIDTH - BORDER_WIDTH;

    // colors
    private static final Color BACKGROUND_COLOR = Color.GRAY;
    private static final Color CLEARED_COLOR = Color.LIGHTGRAY;
    private static final Color UNCLEARED_COLOR = Color.DARKGRAY;
    private static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);
    private static final Color SELECTED_TILE_COLOR = Color.LIGHTBLUE;
    private static final Color MINE_COLOR = Color.YELLOW;
    private static final Color FLAG_COLOR = Color.LIGHTCORAL;

    private final Rectangle background = new Rectangle(BACKGROUND_WIDTH, BACKGROUND_WIDTH, BACKGROUND_COLOR);
    private final Rectangle foreground = new Rectangle(FOREGROUND_WIDTH, FOREGROUND_WIDTH, UNCLEARED_COLOR);

    private final Rectangle decisionLayer = new Rectangle(FOREGROUND_WIDTH, FOREGROUND_WIDTH, TRANSPARENT_COLOR);
    private final Label informationLabel;

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

        setOnMouseEntered(e -> {
            enableHoverColor();
        });

        setOnMouseExited(e -> {
            disableHoverColor();
        });

    }

    /**
     * Method for a user to trigger a tile.
     * Secondary mouse button to flag
     * Primary mouse button to clear
     */
    private void trigger(MouseEvent e) {
        if (minesweeper.isPlaying()) {
            if (e.getButton() == MouseButton.SECONDARY) {
                minesweeperPane.flag(this.row, this.column);
            } else {
                minesweeperPane.clear(this.row, this.column);
            }
        }
    }

    private void enableHoverColor() {
        if (minesweeper.isPlaying() && !minesweeperPane.isBotActive()
            && (minesweeper.getTilingState().isClearable(getCorrespondingTile()))) {
            decisionLayer.setFill(SELECTED_TILE_COLOR);
        }
    }

    private void disableHoverColor() {
        if (minesweeper.isPlaying() && !minesweeperPane.isBotActive()) {
            decisionLayer.setFill(TRANSPARENT_COLOR);
        }
    }

    public void update() {
        Tile currentTile = getCorrespondingTile();
        if (!currentTile.isCleared()) {
            if (currentTile.isFlagged()) {
                foreground.setFill(FLAG_COLOR);
                decisionLayer.setFill(TRANSPARENT_COLOR);
            } else {
                foreground.setFill(UNCLEARED_COLOR);
            }
        } else {
            if (!minesweeper.hasMine(currentTile)) {
                foreground.setFill(CLEARED_COLOR);
                int number = currentTile.getNumberOfNeighboringMines();
                informationLabel.setText(String.format("%s", number == 0 ? "" : number));
                if (number > 0) {
                    informationLabel.setTextFill(numberLabelColors[number - 1]);
                }
            }
        }
    }

    public void reveal(Tile badTile) {
        Tile currentTile = getCorrespondingTile();
        if (minesweeper.hasMine(currentTile) && !currentTile.isFlagged()) {
            foreground.setFill(MINE_COLOR);
            informationLabel.setText("M");
            if (row == badTile.getRow() && column == badTile.getColumn()) {
                foreground.setFill(SELECTED_TILE_COLOR);
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
        decisionLayer.setFill(TRANSPARENT_COLOR);
    }

    public void highlightTileToClear() {
        decisionLayer.setFill(SELECTED_TILE_COLOR);
    }

    private Color calculateGradientColor(double value) {
        Color greenColor = Color.LIGHTGREEN;
        Color redColor = Color.LIGHTCORAL;

        double red = redColor.getRed() * value + greenColor.getRed() * (1 - value);
        double green = redColor.getGreen() * value + greenColor.getGreen() * (1 - value);
        double blue = redColor.getBlue() * value + greenColor.getBlue() * (1 - value);

        return new Color(red, green, blue, 1.0);
    }
}
