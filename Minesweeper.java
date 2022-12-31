import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * Minesweeper game implementation.
 * @author Daniel Öman
 * @date 12/31/2022
 * @version 1.0
 */
public class Minesweeper extends GridPane {

    private Tile[][] tiles;
    private int rows;
    private int columns;
    private int numBombs;
    /** Number of cleared tiles. If this number reaches rows * columns - numBombs, the game is won. */
    private int numCleared = 0;
    private int startingRow;
    private int startingColumn;

    private int flagsRemaining;
    private Label flagLabel;

    /**
     * 3-args constructor
     * @param rows the number of rows
     * @param columns the number of columns
     * @param numBombs the number of bombs
     */
    public Minesweeper(int rows, int columns, int numBombs) {
        setAlignment(Pos.CENTER);
        this.rows = rows;
        this.columns = columns;
        this.numBombs = numBombs;
        flagsRemaining = numBombs;
        flagLabel = new Label(String.format("%d", flagsRemaining));
        tiles = new Tile[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Tile tile = new Tile(i, j);
                tiles[i][j] = tile;
                add(tile, j, i);
            }
        }
    }


    @Override
    public String toString() {
        String minefield = "";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                minefield += tiles[i][j] + " ";
            }
            minefield += "\n";
        }
        return minefield;
    }

    /**
     * Private helper method to check if a position is within bounds.
     * @param row the row of the position to check
     * @param column the column of the position to check
     * @return if the position is within the bounds of the minefield
     */
    private boolean isInBounds(int row, int column) {
        return row >= 0 && row < rows
            && column >= 0 && column < columns
            && row * column <= (rows - 1) * (columns - 1) && row * column >= 0;
    }

    /**
     * Begin the game based on the first tile click
     */
    private void generate() {

        // Begin by randomly generating bombs
        Random rand = new Random();
        for (int i = 0; i < numBombs; i++) {
            int row = rand.nextInt(rows);
            int column = rand.nextInt(columns);

            while (tiles[row][column].hasBomb
                || (row >= startingRow - 1
                && row <= startingRow + 1
                && column >= startingColumn - 1
                && column <= startingColumn + 1)) {
                row = rand.nextInt(rows);
                column = rand.nextInt(columns);
            }
            tiles[row][column].hasBomb = true;
            // now go around and increment the tiles immediately around it
            for (int x = row - 1; x < row + 2; x++) {
                for (int y = column - 1; y < column + 2; y++) {
                    if (!(x == row && y == column) && isInBounds(x, y)) {
                        tiles[x][y].numAdjacent += 1;
                    }
                }
            }
        }
    }

    /**
     * Flag Label getter method
     * @return this game's number of flags in a label
     */
    public Label getFlagLabel() {
        return flagLabel;
    }

    /**
     * Inner class to represent a tile on the minefield.
     */
    private class Tile extends StackPane {
        private int row;
        private int column;
        private int numAdjacent;
        private boolean hasBomb;
        private boolean isCleared;
        private boolean flagged;
        private final Rectangle background = new Rectangle(30, 30, Color.DARKGRAY);
        private Rectangle foreground = new Rectangle(25, 25, Color.LIGHTGRAY);
        private boolean didDoubleClick;

        /**
         * Constructor for a tile.
         * This constructor is most accurately used after populating bombs set.
         * @param row the row the tile is on
         * @param column the column the tile is on
         */
        public Tile(int row, int column) {
            isCleared = false;
            hasBomb = false;
            didDoubleClick = false;
            getChildren().add(background);
            getChildren().add(foreground);

            setOnContextMenuRequested(e -> {
                didDoubleClick = true;
                if (!isCleared) {
                    if (!flagged) {
                        getChildren().add(new Flag());
                        flagsRemaining--;
                        flagLabel.setText(String.format("%d", flagsRemaining));

                        flagged = true;
                    } else {
                        getChildren().remove(getChildren().size() - 1);
                        flagsRemaining++;
                        flagLabel.setText(String.format("%d", flagsRemaining));
                        flagged = false;
                    }
                }
            });

            setOnMouseClicked(e -> {
                if (didDoubleClick) {
                    didDoubleClick = false;
                } else {
                    if (!isCleared && !flagged) {
                        if (numCleared == 0) {
                            startingRow = row;
                            startingColumn = column;
                            Minesweeper.this.generate();
                        }
                        boolean result = clearTiles();
                        System.out.println(numCleared);
                        if (!result) {
                            System.out.println("You Lost!");
                        } else if (numCleared == rows * columns - numBombs) {
                            System.out.println("You win!");
                        }
                    }
                }
            });
            this.row = row;
            this.column = column;
        }

        /**
         * Method to clear a tile.
         * Calls a recursive helper.
         * @return true if clear was successful (not a bomb), false otherwise
         */
        public boolean clearTiles() {
            if (hasBomb) {
                numCleared++;
                isCleared = true;
                foreground.setFill(Color.DARKGOLDENROD);
                getChildren().add(new Label("B"));
                return false;
            }
            Set<Tile> visited = new HashSet<Tile>();
            clearTiles(this, visited);
            return true;
        }

        /**
         * Recursive helper method to clear the tiles using DFS
         * @param curr the current tile we are clearing
         * @param visited the set of visited tiles
         */
        private void clearTiles(Tile curr, Set<Tile> visited) {
            if (!visited.contains(curr) && !curr.flagged) {
                visited.add(curr);
                curr.isCleared = true;
                curr.foreground.setFill(Color.GREENYELLOW);
                curr.getChildren().add(
                    new Label(String.format("%s", curr.numAdjacent == 0 ? " " : curr.numAdjacent))
                );
                numCleared++;
                if (curr.numAdjacent == 0) {
                    for (int i = curr.row - 1; i < curr.row + 2; i++) {
                        for (int j = curr.column - 1; j < curr.column + 2; j++) {
                            if (!(i == curr.row && j == curr.column) && isInBounds(i, j)) {
                                clearTiles(tiles[i][j], visited);
                            }
                        }
                    }
                }
            }
        }
        @Override
        public String toString() {
            return hasBomb ? "B" : String.format("%d", numAdjacent);
        }
    }

    private class Flag extends StackPane {
        /**
         * No args constructor for a flag
         */
        public Flag() {
            getChildren().add(new Circle(10, Color.RED));
        }
    }
}