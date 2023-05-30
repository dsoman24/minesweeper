package src.main.java.minesweeper.ui.gamedata;

import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.main.java.minesweeper.logic.Difficulty;
import src.main.java.minesweeper.logic.GameSummary;
import src.main.java.minesweeper.logic.Status;

public class StatsStage extends Stage {

    public StatsStage(Difficulty difficulty) {
        VBox statsRoot = new VBox();
        if (difficulty == null) {
            difficulty = Difficulty.EASY;
        }
        setTitle("Statistics for " + difficulty);
        List<GameSummary> data = GameDataIO.read("data.csv", difficulty);

        int gamesPlayed = gamesPlayed(data);
        double winRate = winRate(data) * 100;
        double bestTime = bestTime(data);

        Label gamesPlayedLabel = new Label("Games played: " + gamesPlayed);
        Label winRateLabel = new Label(String.format("Win Rate: %.2f%%", winRate));
        Label bestTimeLabel = new Label(String.format("Best Time: %.2fs", bestTime));

        statsRoot.getChildren().add(gamesPlayedLabel);
        statsRoot.getChildren().add(winRateLabel);
        statsRoot.getChildren().add(bestTimeLabel);
        statsRoot.setAlignment(Pos.CENTER);
        statsRoot.setSpacing(10);

        setWidth(500);
        setHeight(140);
        Scene statScene = new Scene(statsRoot);
        setScene(statScene);
    }

    private double bestTime(List<GameSummary> data) {
        long min = gamesPlayed(data) == 0 ? 0 : data.get(0).getElapsedMillis();
        for (GameSummary gameSummary : data) {
            if (gameSummary.getElapsedMillis() < min) {
                min = gameSummary.getElapsedMillis();
            }
        }
        double bestTime = min / 1000.0;
        return bestTime;
    }

    private int gamesPlayed(List<GameSummary> data) {
        return data.size();
    }

    private int gamesWon(List<GameSummary> data) {
        int count = 0;
        for (GameSummary summary : data) {
            if (summary.getStatus() == Status.WIN) {
                count++;
            }
        }
        return count;
    }

    public double winRate(List<GameSummary> data) {
        if (gamesPlayed(data) > 0) {
            return (double) gamesWon(data) / gamesPlayed(data);
        } else {
            return 0;
        }
    }
}
