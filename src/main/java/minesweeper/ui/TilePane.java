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

    // sprite layer
    private final Rectangle spriteLayer;

    // colors
    private static final double DECISION_OPACITY = 0.3;
    private static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);
    private static final Color SELECTED_TILE_COLOR = new Color(0, 0, 0.5, DECISION_OPACITY);

    private final Rectangle decisionLayer = new Rectangle(TileSprite.SIZE, TileSprite.SIZE, TRANSPARENT_COLOR);
    private final Label informationLabel;


    public TilePane(int row, int column, MinesweeperPane minesweeperPane) {

        spriteLayer = new Rectangle(TileSprite.SIZE, TileSprite.SIZE);
        updateSprite(TileSprite.UNCLEARED);

        this.row = row;
        this.column = column;
        this.minesweeperPane = minesweeperPane;
        this.minesweeper = minesweeperPane.getMinesweeper();
        informationLabel = new Label();
        informationLabel.setFont(Font.font(9));

        getChildren().add(spriteLayer);
        getChildren().add(decisionLayer);
        getChildren().add(informationLabel);

        setOnMouseClicked(e -> {
            trigger(e);
        });

        setOnMouseEntered(e -> {
            if (minesweeper.isPlaying() && !minesweeperPane.isBotActive()) {
                enableHoverColor();
            }
        });

        setOnMouseExited(e -> {
            if (minesweeper.isPlaying() && !minesweeperPane.isBotActive()) {
                disableHoverColor();
            }
        });

    }

    private void updateSprite(TileSprite sprite) {
        spriteLayer.setFill(sprite.getImagePattern());
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

    public void enableHoverColor() {
        if (minesweeper.getTilingState().isClearable(getCorrespondingTile())) {
            decisionLayer.setFill(SELECTED_TILE_COLOR);
        }
    }

    public void disableHoverColor() {
        decisionLayer.setFill(TRANSPARENT_COLOR);
    }

    public void update() {
        Tile currentTile = getCorrespondingTile();
        if (!currentTile.isCleared()) {
            if (currentTile.isFlagged()) {
                decisionLayer.setFill(TRANSPARENT_COLOR);
                updateSprite(TileSprite.FLAG);
            } else {
                updateSprite(TileSprite.UNCLEARED);
            }
        } else {
            if (!minesweeper.hasMine(currentTile)) {
                int number = currentTile.getNumberOfNeighboringMines();
                updateSprite(TileSprite.getClearedNumberedSprite(number));
            }
        }
    }

    public void reveal(Tile badTile) {
        Tile currentTile = getCorrespondingTile();
        if (minesweeper.hasMine(currentTile) && !currentTile.isFlagged()) {
            updateSprite(TileSprite.MINE);
            if (row == badTile.getRow() && column == badTile.getColumn()) {
                updateSprite(TileSprite.MINE_TRIGGERED);

            }
        } else if (!minesweeper.hasMine(currentTile) && currentTile.isFlagged()) {
            updateSprite(TileSprite.FLAG_INCORRECT);
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
        if (!getCorrespondingTile().isCleared()) {
            informationLabel.setText("");
        }
        decisionLayer.setFill(TRANSPARENT_COLOR);
    }

    public void updateNumericalOverlayIfCleared() {
        if (getCorrespondingTile().isCleared()) {
            decisionLayer.setFill(TRANSPARENT_COLOR);
            informationLabel.setText("");
        }
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

        return new Color(red, green, blue, DECISION_OPACITY);
    }
}
