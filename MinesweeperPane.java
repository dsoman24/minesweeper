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

    private Difficulty difficulty;

    public MinesweeperPane(Difficulty difficulty, Stage gameStage) {
        setAlignment(Pos.CENTER);
        this.difficulty = difficulty;
        this.gameStage = gameStage;
        this.minesweeper = new Minesweeper(difficulty);
        flagLabel = new Label(String.format("Flags remaining:\n%d", minesweeper.getFlagsRemaining()));
        flagLabel.setAlignment(Pos.CENTER);
        secondsLabel = new Label("0 s");
        secondsLabel.setAlignment(Pos.CENTER);
        secondsLabel.setMinWidth(70);
        gameStage.setTitle(String.format("Minesweeper %s", difficulty.toString()));
        setStageSize();
        timer = new Timer();
        tilePanes = new TilePane[minesweeper.getNumRows()][minesweeper.getNumColumns()];

        for (int i = 0; i < minesweeper.getNumRows(); i++) {
            for (int j = 0; j < minesweeper.getNumColumns(); j++) {
                TilePane tilePane = new TilePane(i, j, this);
                tilePanes[i][j] = tilePane;
                add(tilePane, j, i); // this adds to gridpane
            }
        }
    }

    private void setStageSize() {
        gameStage.setHeight(30 * minesweeper.getNumRows() + 120);
        gameStage.setWidth(30 * minesweeper.getNumColumns() + 100);
    }

    public void clear(int row, int column) {
        if (minesweeper.isPlaying()) {
            if (minesweeper.getNumTilesCleared() == 0) {
                timer.start();
            }
            minesweeper.clear(row, column);
            update();
            if (!minesweeper.isPlaying()) {
                if (minesweeper.getNumTilesCleared() == minesweeper.getNumRows() * minesweeper.getNumColumns() - minesweeper.getNumMines()) {
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
            flagLabel.setText(String.format("Flags remaining:\n%d", minesweeper.getFlagsRemaining()));
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

            LeaderboardEntry entry = new LeaderboardEntry(name, difficulty, time);

            LeaderboardWriter leaderBoardWriter = new LeaderboardWriter();
            leaderBoardWriter.write(entry);

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
