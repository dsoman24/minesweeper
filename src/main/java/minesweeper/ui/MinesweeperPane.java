package src.main.java.minesweeper.ui;
// import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import src.main.java.minesweeper.bot.Bot;
import src.main.java.minesweeper.bot.strategy.StrategyTypeOnTile;
import src.main.java.minesweeper.logic.Difficulty;
import src.main.java.minesweeper.logic.Minesweeper;
import src.main.java.minesweeper.logic.Status;
import src.main.java.minesweeper.logic.Tile;

/**
 * Implement game functionality for minesweeper.
 */
public class MinesweeperPane extends GridPane {

    private Minesweeper minesweeper;
    private TilePane[][] tilePanes;

    private Timer timer;
    private Label secondsLabel;
    private Label flagLabel;

    private HBox botInput;

    private Difficulty difficulty;

    private GameStage gameStage;

    private boolean botActivated = false;
    private boolean overlayAdded = false;

    private int botDelay = 300; // 1000 is one second, delay in milliseconds

    public MinesweeperPane(Difficulty difficulty, GameStage gameStage) {
        this.gameStage = gameStage;
        setAlignment(Pos.CENTER);
        this.difficulty = difficulty;

        this.minesweeper = new Minesweeper(difficulty);
        flagLabel = new Label();
        updateFlagText();
        flagLabel.setAlignment(Pos.CENTER);
        secondsLabel = new Label("0 s");
        secondsLabel.setAlignment(Pos.CENTER);
        secondsLabel.setMinWidth(70);
        timer = new Timer(secondsLabel);
        tilePanes = new TilePane[minesweeper.getNumRows()][minesweeper.getNumberOfColumns()];

        ComboBox<StrategyTypeOnTile> botStrategySelector = new ComboBox<>();
        for (StrategyTypeOnTile strategyType : StrategyTypeOnTile.values()) {
            botStrategySelector.getItems().add(strategyType);
        }
        botStrategySelector.setPromptText("Bot Strategy");
        TextField botDelayTextField = new TextField();
        botDelayTextField.setPromptText("Delay (ms)");

        // Bot management here
        Button toggleBot = new Button("Start Bot");
        toggleBot.setOnAction(e -> {
            if (minesweeper.isPlaying()) {
                if (botActivated) {
                    toggleBot.setText("Start Bot");
                } else {
                    toggleBot.setText("Stop Bot");
                    String delayValue = botDelayTextField.getText();
                    if (delayValue != null && !delayValue.isBlank()) {
                        try {
                            botDelay = Integer.parseInt(delayValue);
                        } catch (NumberFormatException nfe) {}
                    }

                    // remove all flags once before running the bot
                    if (minesweeper.removeAllFlags()) {
                        update();
                    }

                    StrategyTypeOnTile strategyType = botStrategySelector.getValue();
                    if (strategyType == null) {
                        strategyType = StrategyTypeOnTile.PROBABILISTIC;
                    }

                    Bot<Tile> bot = new Bot<>(minesweeper.getTilingState(), strategyType.getStrategy());
                    BotRunner botRunner = new BotRunner(bot);
                    Thread botThread = new Thread(botRunner);
                    botThread.start();
                }
                botActivated = !botActivated;
            }
        });

        botInput = new HBox();
        botInput.getChildren().addAll(botStrategySelector, botDelayTextField, toggleBot);

        for (int i = 0; i < minesweeper.getNumRows(); i++) {
            for (int j = 0; j < minesweeper.getNumberOfColumns(); j++) {
                TilePane tilePane = new TilePane(i, j, this);
                tilePanes[i][j] = tilePane;
                add(tilePane, j, i); // this adds to gridpane
            }
        }
    }

    /**
     * Clear a tile, human player input only.
     */
    public void clear(int row, int column) {
        if (userCanInteract()) {
            if (minesweeper.getNumberOfTilesCleared() == 0) {
                timer.start();
            }
            minesweeper.clear(row, column);
            update();
            if (minesweeper.status() == Status.WIN) {
                winAction();
            } else if (minesweeper.status() == Status.LOSE) {
                loseAction(minesweeper.getTileAt(row, column));
            }
        }
    }

    /**
     * Toggle flag at a row and column, human player input only.
     */
    public void flag(int row, int column) {
        if (userCanInteract()) { // can only flag if we are playing
            minesweeper.flag(row, column);
            updateFlagText();
            tilePanes[row][column].update(); // only need to update individual tile
        }
    }

    private boolean userCanInteract() {
        return minesweeper.isPlaying() && !botActivated;
    }

    private void updateFlagText() {
        flagLabel.setText(String.format("Flags remaining:\n%d", minesweeper.getFlagsRemaining()));
    }

    /**
     * GUI activity when the game is won
     */
    private void winAction() {
        timer.stop();
        WinStage winningStage = new WinStage(timer, gameStage, difficulty);
        winningStage.show();
    }

    /**
     * GUI activity when the game is lost
     */
    private void loseAction(Tile badTile) {
        timer.stop();
        reveal(badTile);
        LoseStage losingStage = new LoseStage(gameStage);
        losingStage.show();
    }

    /**
     * Updates all tiles in the stage, done after activating a tile.
     */
    private void update() {
        for (int i = 0; i < minesweeper.getNumRows(); i++) {
            for (int j = 0; j < minesweeper.getNumberOfColumns(); j++) {
                tilePanes[i][j].update();
            }
        }
        updateFlagText();
    }

    /**
     * Reveals all bombs in the stage, called when losing.
     */
    private void reveal(Tile badTile) {
        for (int i = 0; i < minesweeper.getNumRows(); i++) {
            for (int j = 0; j < minesweeper.getNumberOfColumns(); j++) {
                tilePanes[i][j].reveal(badTile);
            }
        }
    }

    public Minesweeper getMinesweeper() {
        return minesweeper;
    }

    public Label getFlagLabel() {
        return flagLabel;
    }

    public Label getSecondsLabel() {
        return secondsLabel;
    }

    public HBox getBotInput() {
        return botInput;
    }

    private void applyDecisionOverlay(Map<Tile, ? extends Number> decisionDetails) {
        if (!overlayAdded) {
            overlayAdded = true;
            for (Tile tile : decisionDetails.keySet()) {
                Number value = decisionDetails.get(tile);
                tilePanes[tile.getRow()][tile.getColumn()].addNumericalOverlay(value);
            }
        }
    }

    private void clearDecisionOverlay() {
        if (overlayAdded) {
            overlayAdded = false;
            for (TilePane[] row : tilePanes) {
                for (TilePane tilePane : row) {
                    tilePane.removeNumericalOverlay();
                }
            }
        }
    }

    private void highlightTileToClear(Tile tile) {
        tilePanes[tile.getRow()][tile.getColumn()].highlightTileToClear();
    }

    private class BotRunner implements Runnable {

        private Bot<Tile> bot;

        public BotRunner(Bot<Tile> bot) {
            this.bot = bot;
        }

        @Override
        public void run() {
            while (botActivated && minesweeper.status() == Status.PLAYING) {
                if (minesweeper.getNumberOfTilesCleared() == 0) {
                    timer.start();
                }

                // find the tile to clear
                Tile tileToClear = bot.tileToClear();

                // apply decision details to tiles
                Map<Tile, ? extends Number> decisionDetails = bot.decisionDetails();

                // add decision overlay
                Platform.runLater(() -> applyDecisionOverlay(decisionDetails));

                // highlight the tile we are about to clear
                Platform.runLater(() -> highlightTileToClear(tileToClear));

                // "thinking" delay time.
                try {
                    Thread.sleep(botDelay);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }

                // add the decision details on the UI thread

                // apply flags to the tiles that we want to flag
                // List<Tile> tilesToFlag = bot.tilesToFlag();
                // for (Tile tile : tilesToFlag) {
                //     tile.setFlag(true);
                // }

                // clear the tile
                minesweeper.clear(tileToClear.getRow(), tileToClear.getColumn());

                Platform.runLater(() -> clearDecisionOverlay());

                // update UI
                Platform.runLater(() -> update()); // Update the UI on the UI thread

                // check win/lose condition
                if (minesweeper.status() == Status.WIN) {
                    Platform.runLater(() -> winAction()); // Run the win action on the UI thread
                } else if (minesweeper.status() == Status.LOSE) {
                    Platform.runLater(() -> loseAction(tileToClear)); // Run the lose action on the UI thread
                }
            }
        }
    }
}
