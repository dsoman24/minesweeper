import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

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
    private int numMines;

    private int startingRow;
    private int startingColumn;

    private int flagsRemaining;
    private Label flagLabel;

    private Timer timer;
    private Label secondsLabel;

    // if the game is currently in play
    private boolean playing;
    private int numCleared;

    private Stage gameStage;

    /**
     * 3-args constructor
     * @param rows the number of rows
     * @param columns the number of columns
     * @param numMines the number of mines
     * @param gameStage the stage that this game is played on
     */
    public Minesweeper(int rows, int columns, int numMines, Stage gameStage) {
        setAlignment(Pos.CENTER);
        playing = true;
        this.rows = rows;
        this.columns = columns;
        this.numMines = numMines;
        this.gameStage = gameStage;
        numCleared = 0;
        flagsRemaining = numMines;
        flagLabel = new Label(String.format("Flags remainging:\n%d", flagsRemaining));
        flagLabel.setAlignment(Pos.CENTER);
        tiles = new Tile[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Tile tile = new Tile(i, j);
                tiles[i][j] = tile;
                add(tile, j, i);
            }
        }
        secondsLabel = new Label("0 s");
        secondsLabel.setAlignment(Pos.CENTER);
        secondsLabel.setMinWidth(70);
        timer = new Timer();
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
     * Generate the game state on the first click. Will guarantee a 0 on start.
     */
    private void startGame() {
        // Begin by randomly generating mines
        Random rand = new Random();
        for (int i = 0; i < numMines; i++) {
            int row = rand.nextInt(rows);
            int column = rand.nextInt(columns);

            while (tiles[row][column].hasMine
                || (row >= startingRow - 1
                && row <= startingRow + 1
                && column >= startingColumn - 1
                && column <= startingColumn + 1)) {
                row = rand.nextInt(rows);
                column = rand.nextInt(columns);
            }
            tiles[row][column].hasMine = true;
            // now go around and increment the tiles immediately around it
            for (int x = row - 1; x < row + 2; x++) {
                for (int y = column - 1; y < column + 2; y++) {
                    if (!(x == row && y == column) && isInBounds(x, y)) {
                        tiles[x][y].neighboringMines += 1;
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
     * Seconds label getter method
     * @return this game's seconds label
     */
    public Label getSecondsLabel() {
        return secondsLabel;
    }

    /**
     * Method to clear a tile from the board.
     * Utilized in tile clicking functionality.
     * TO-DO use in bot functionality
     * @param row the row of the tile to clear
     * @param column the column of the tile to clear
     */
    public void clear(int row, int column) {
        Tile currentTile = tiles[row][column];
        if (playing) {
            if (!currentTile.isCleared && !currentTile.hasFlag) {
                if (numCleared == 0) {
                    startingRow = row;
                    startingColumn = column;
                    Minesweeper.this.startGame();
                    timer.start();
                }
                playing = currentTile.clearTiles();
                if (!playing) {
                    timer.stop();
                    // reveal all bombs upon losing
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < columns; j++) {
                            if (tiles[i][j].hasMine && !tiles[i][j].hasFlag) {
                                tiles[i][j].clearTiles();
                            } else if (!tiles[i][j].hasMine && tiles[i][j].hasFlag) {
                                tiles[i][j].getChildren().add(new Label("X"));
                            }
                        }
                    }
                    Stage losingStage = new Stage();
                    losingStage.setTitle("You Lose");
                    VBox root = new VBox();

                    Label loseLabel = new Label("You Lose");

                    Button loseButton = new Button("Exit");

                    loseButton.setOnAction(a -> {
                        losingStage.close();
                        gameStage.close();
                    });

                    root.getChildren().addAll(loseLabel, loseButton);
                    root.setAlignment(Pos.CENTER);
                    root.setPadding(new Insets(10));
                    root.setSpacing(10);
                    Scene scene = new Scene(root);
                    losingStage.setScene(scene);
                    losingStage.setWidth(200);
                    losingStage.show();
                } else if (numCleared == rows * columns - numMines) {
                    timer.stop();
                    playing = false;

                    Stage winningStage = new Stage();
                    winningStage.setTitle("You Win!");

                    VBox root = new VBox();

                    Label winLabel = new Label(
                        String.format("[%d.%d s] Enter your name to enter the leaderboard",
                        timer.elapsedSeconds, timer.milliseconds)
                    );
                    TextField nameField = new TextField();
                    nameField.setPromptText("Enter your name");

                    Button saveToLeaderboard = new Button("Submit");
                    saveToLeaderboard.setOnAction(e -> {
                        String name = nameField.getText();
                        double time = Double.parseDouble(
                            String.format("%d.%d", timer.elapsedSeconds, timer.milliseconds)
                        );
                        File leaderboard = new File("leaderboard.csv");
                        PrintWriter writer;
                        try {
                            boolean newFile = !leaderboard.exists();
                            writer = new PrintWriter(
                                new FileOutputStream(leaderboard, true)
                            );
                            if (newFile) {
                                writer.println("date,name,rows,columns,numMines,time");
                            }
                            writer.println(new LeaderboardEntry(name, rows, columns, numMines, time));
                            writer.close();
                        } catch (FileNotFoundException fnfe) {
                            System.out.println("File not found");
                        }
                        winningStage.close();
                        gameStage.close();
                    });

                    root.getChildren().addAll(winLabel, nameField, saveToLeaderboard);
                    root.setAlignment(Pos.CENTER);
                    root.setPadding(new Insets(10));
                    root.setSpacing(10);
                    Scene scene = new Scene(root);
                    winningStage.setScene(scene);
                    winningStage.show();
                }
            }
        }
    }

    /**
     * Method to flag a tile
     * Utilized in tile clicking functionality.
     * TO-DO use in bot functionality
     * @param row the row of the tile to flag
     * @param column the column of the tile to flag
     */

    public void flag(int row, int column) {
        Tile currentTile = tiles[row][column];
        if (playing) {
            if (!currentTile.isCleared) {
                if (!currentTile.hasFlag) {
                    currentTile.getChildren().add(new Flag());
                    flagsRemaining--;
                    currentTile.hasFlag = true;
                } else {
                    currentTile.getChildren().remove(currentTile.getChildren().size() - 1);
                    flagsRemaining++;
                    currentTile.hasFlag = false;
                }
                flagLabel.setText(String.format("Flags remainging:\n%d", flagsRemaining));
            }
        }
    }

    /**
     * Inner class to represent a tile on the minefield.
     */
    private class Tile extends StackPane {
        private int row;
        private int column;
        private int neighboringMines;
        private boolean hasMine;
        private boolean hasFlag;
        private boolean isCleared;
        private final Rectangle background = new Rectangle(30, 30, Color.DARKGRAY);
        private Rectangle foreground = new Rectangle(25, 25, Color.LIGHTGRAY);

        /**
         * Constructor for a tile.
         * This constructor is most accurately used after populating mines set.
         * @param row the row the tile is on
         * @param column the column the tile is on
         */
        public Tile(int row, int column) {
            this.row = row;
            this.column = column;
            hasMine = false;
            isCleared = false;
            getChildren().add(background);
            getChildren().add(foreground);


            setOnMouseClicked(e -> {
                if (playing) {
                    if (e.getButton() == MouseButton.SECONDARY) {
                        flag(row, column);
                    } else {
                        clear(row, column);
                    }
                }
            });
        }

        /**
         * Method to clear a tile.
         * Calls a recursive helper.
         * @return true if clear was successful (not a mine), false otherwise
         */
        public boolean clearTiles() {
            if (hasMine) {
                foreground.setFill(Color.DARKGOLDENROD);
                getChildren().add(new Label("M"));
                return false;
            }
            clearTiles(this);
            return true;
        }

        /**
         * Recursive helper method to clear the tiles using DFS.
         * Clears all adjacent tiles if not visited, stops after it clears a non-zero or flagged tile.
         * @param curr the current tile we are clearing
         */
        private void clearTiles(Tile curr) {
            if (!curr.isCleared && !curr.hasFlag) {
                curr.isCleared = true;
                numCleared++;
                curr.foreground.setFill(Color.GREENYELLOW);
                curr.getChildren().add(
                    new Label(String.format("%s", curr.neighboringMines == 0 ? " " : curr.neighboringMines))
                );
                if (curr.neighboringMines == 0) {
                    for (int i = curr.row - 1; i < curr.row + 2; i++) {
                        for (int j = curr.column - 1; j < curr.column + 2; j++) {
                            if (!(i == curr.row && j == curr.column) && isInBounds(i, j)) {
                                clearTiles(tiles[i][j]);
                            }
                        }
                    }
                }
            }
        }
        @Override
        public String toString() {
            return hasMine ? "B" : String.format("%d", neighboringMines);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Tile) {
                Tile other = (Tile) o;
                return this.row == other.row
                    && this.column == other.column
                    && this.neighboringMines == other.neighboringMines
                    && this.hasMine == other.hasMine;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return row + column + neighboringMines;
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

    private class Timer extends AnimationTimer {
        private long startTime;
        private long elapsedNanos;
        private long elapsedSeconds;
        private long milliseconds;

        @Override
        public void start() {
            startTime = System.nanoTime();
            super.start();
        }

        @Override
        public void handle(long now) {
            elapsedNanos = now - startTime;
            elapsedSeconds = elapsedNanos / 1000000000;
            milliseconds = (elapsedNanos / 1000000) % 1000;
            secondsLabel.setText(String.format("%d s", elapsedSeconds));
        }
    };
}
