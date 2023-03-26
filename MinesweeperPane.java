import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MinesweeperPane extends GridPane {

    private Minesweeper minesweeper;
    private TilePane[][] tilePanes;

    private Timer timer;
    private Label secondsLabel;
    private Label flagLabel;

    private Stage gameStage;


    public MinesweeperPane(int rows, int columns, int numMines, Stage gameStage) {
        setAlignment(Pos.CENTER);
        this.gameStage = gameStage;
        this.minesweeper = new Minesweeper(rows, columns, numMines);
        flagLabel = new Label(String.format("Flags remainging:\n%d", minesweeper.getFlagsRemaining()));
        flagLabel.setAlignment(Pos.CENTER);
        secondsLabel = new Label("0 s");
        secondsLabel.setAlignment(Pos.CENTER);
        secondsLabel.setMinWidth(70);
        timer = new Timer();
        tilePanes = new TilePane[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                TilePane tilePane = new TilePane(i, j, this);
                tilePanes[i][j] = tilePane;
                add(tilePane, j, i); // this adds to gridpane
            }
        }
    }

    public void clear(int row, int column) {
        if (minesweeper.isPlaying()) {
            if (minesweeper.getNumCleared() == 0) {
                timer.start();
            }
            minesweeper.clear(row, column);
            update();
            if (!minesweeper.isPlaying()) {
                if (minesweeper.getNumCleared() == minesweeper.getNumRows() * minesweeper.getNumColumns() - minesweeper.getNumMines()) {
                    winAction();
                } else {
                    loseAction();
                }
            }
        }
    }

    public void flag(int row, int column) {
        if (minesweeper.isPlaying()) { // can only flag if we are playing
            minesweeper.flag(row, column);
            tilePanes[row][column].update(); // only need to update individual tile
        }

    }

    public void winAction() {
        timer.stop();
        Stage winningStage = new Stage();
        winningStage.setTitle("You Win!");

        VBox root = new VBox();

        Label winLabel = new Label(
            String.format("[%d.%d s] Enter your name to enter the leaderboard",
            timer.elapsedSeconds, timer.elapsedMilliseconds)
        );
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");

        Button saveToLeaderboard = new Button("Submit");
        saveToLeaderboard.setOnAction(e -> {
            String name = nameField.getText();
            double time = Double.parseDouble(
                String.format("%d.%d", timer.elapsedSeconds, timer.elapsedMilliseconds)
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
                writer.println(new LeaderboardEntry(name, minesweeper.getNumRows(), minesweeper.getNumColumns(), minesweeper.getNumMines(), time));
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


    public void loseAction() {
        timer.stop();

        // reveal all bombs upon losing
        reveal();

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
    }

    public void update() {
        for (int i = 0; i < minesweeper.getNumRows(); i++) {
            for (int j = 0; j < minesweeper.getNumColumns(); j++) {
                tilePanes[i][j].update();
            }
        }
    }

    public void reveal() {
        for (int i = 0; i < minesweeper.getNumRows(); i++) {
            for (int j = 0; j < minesweeper.getNumColumns(); j++) {
                tilePanes[i][j].reveal();
            }
        }
    }

    public Minesweeper getMinesweeper() {
        return minesweeper;
    }


    private class Timer extends AnimationTimer {
        private long startTime;
        private long elapsedNanos;
        private long elapsedSeconds;
        private long elapsedMilliseconds;

        @Override
        public void start() {
            startTime = System.nanoTime();
            super.start();
        }

        @Override
        public void handle(long now) {
            elapsedNanos = now - startTime;
            elapsedSeconds = elapsedNanos / 1000000000;
            elapsedMilliseconds = (elapsedNanos / 1000000) % 1000;
            secondsLabel.setText(String.format("%d s", elapsedSeconds));
        }
    };


    public Label getFlagLabel() {
        return flagLabel;
    }

    public Label getSecondsLabel() {
        return secondsLabel;
    }

}
