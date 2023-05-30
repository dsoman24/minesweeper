package src.main.java.minesweeper.ui;
import java.util.Map;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import src.main.java.minesweeper.ui.gamedata.GameDataIO;

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

    private volatile boolean botActivated = false;
    private volatile boolean overlayAdded = false;
    private boolean eligibleToBeSaved = true;

    private Thread botThread;

    private int botDelay = 100; // 1000 is one second, delay in milliseconds

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
                    clearDecisionOverlay();
                    toggleBot.setText("Start Bot");
                } else {
                    eligibleToBeSaved = false;
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
                    botThread = new Thread(botRunner);
                    botThread.start();
                }
                botActivated = !botActivated;
            }
        });

        CheckBox displayBotDetailsCheckBox = new CheckBox();
        displayBotDetailsCheckBox.setOnMouseClicked(e -> {
            overlayAdded = displayBotDetailsCheckBox.isSelected();
            if (!overlayAdded) {
                clearDecisionOverlay();
            }
        });

        Label displayBotDetailsLabel = new Label("Show Metrics");

        botInput = new HBox();
        botInput.getChildren().addAll(botStrategySelector, botDelayTextField, toggleBot, displayBotDetailsCheckBox, displayBotDetailsLabel);
        botInput.setSpacing(10);
        botInput.setAlignment(Pos.CENTER);

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
            Tile tile = minesweeper.getTileAt(row, column);
            if (minesweeper.getNumberOfTilesCleared() == 0) {
                timer.start();
            }
            if (!tile.isCleared()) {
                minesweeper.clear(tile);
            } else if (tile.getNumberOfNeighboringMines() > 0) {
                minesweeper.multiClear(tile);
            }
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
        flagLabel.setText(String.format("Flags: %d", minesweeper.getFlagsRemaining()));
    }

    /**
     * GUI activity when the game is won
     */
    private void winAction() {
        timer.stop();
        if (eligibleToBeSaved) {
            GameDataIO.write("data.csv", minesweeper.getSummary());
        }
        WinStage winningStage = new WinStage(timer, gameStage, difficulty);
        winningStage.show();
    }

    /**
     * GUI activity when the game is lost
     */
    private void loseAction(Tile badTile) {
        timer.stop();
        reveal(badTile);
        if (eligibleToBeSaved) {
            GameDataIO.write("data.csv", minesweeper.getSummary());
        }
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

    public boolean isBotActive() {
        return botActivated;
    }

    private void applyDecisionOverlay(Map<Tile, ? extends Number> decisionDetails) {
        for (Tile tile : decisionDetails.keySet()) {
            Number value = decisionDetails.get(tile);
            tilePanes[tile.getRow()][tile.getColumn()].addNumericalOverlay(value);
        }
    }

    private void clearDecisionOverlay() {
        for (TilePane[] row : tilePanes) {
            for (TilePane tilePane : row) {
                tilePane.removeNumericalOverlay();
            }
        }
    }

    private void updateDecisionOverlay() {
        for (TilePane[] row : tilePanes) {
            for (TilePane tilePane : row) {
                tilePane.updateNumericalOverlayIfCleared();
            }
        }
    }

    private void enableHoverColor(Tile tile) {
        tilePanes[tile.getRow()][tile.getColumn()].enableHoverColor();
    }

    private void disableHoverColor(Tile tile) {
        tilePanes[tile.getRow()][tile.getColumn()].disableHoverColor();
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
                if (overlayAdded) {
                    Platform.runLater(() -> applyDecisionOverlay(decisionDetails));
                }

                // highlight the tile we are about to clear
                Platform.runLater(() -> enableHoverColor(tileToClear));

                // "thinking" delay time.
                try {
                    Thread.sleep(botDelay);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }

                // apply flags to the tiles that we want to flag
                // List<Tile> tilesToFlag = bot.tilesToFlag();
                // for (Tile tile : tilesToFlag) {
                //     tile.setFlag(true);
                // }

                // remove the decision hover color
                Platform.runLater(() -> disableHoverColor(tileToClear));
                Platform.runLater(() -> updateDecisionOverlay());

                // clear the tile
                minesweeper.clear(tileToClear);
                // update UI
                Platform.runLater(() -> update()); // Update the UI on the UI thread

                // check win/lose condition
                if (!minesweeper.isPlaying()) {
                    Platform.runLater(() -> clearDecisionOverlay());
                    if (minesweeper.status() == Status.WIN) {
                        Platform.runLater(() -> winAction()); // Run the win action on the UI thread
                    } else if (minesweeper.status() == Status.LOSE) {
                        Platform.runLater(() -> loseAction(tileToClear)); // Run the lose action on the UI thread
                    }
                }
            }
        }
    }
}
