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
        double meanTimeWin = meanTimeWin(data);
        double meanTimeLose = meanTimeLose(data);

        Label gamesPlayedLabel = new Label("Games played: " + gamesPlayed);
        Label winRateLabel = new Label(String.format("Win Rate: %.2f%%", winRate));
        Label bestTimeLabel = new Label(String.format("Best Time: %.2fs", bestTime));
        Label meanTimeLabelWin = new Label(String.format("Mean Time (wins): %.2fs", meanTimeWin));
        Label meanTimeLabelLose = new Label(String.format("Mean Time (losses): %.2fs", meanTimeLose));

        statsRoot.getChildren().add(gamesPlayedLabel);
        statsRoot.getChildren().add(winRateLabel);
        statsRoot.getChildren().add(bestTimeLabel);
        statsRoot.getChildren().add(meanTimeLabelWin);
        statsRoot.getChildren().add(meanTimeLabelLose);
        statsRoot.setAlignment(Pos.CENTER);
        statsRoot.setSpacing(10);

        setWidth(500);
        setHeight(200);
        Scene statScene = new Scene(statsRoot);
        setScene(statScene);
    }

    private double bestTime(List<GameSummary> data) {
        long min = gamesPlayed(data) == 0 ? 0 : data.get(0).getElapsedMillis();
        for (GameSummary gameSummary : data) {
            if (gameSummary.getStatus() == Status.WIN && gameSummary.getElapsedMillis() < min) {
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

    private double winRate(List<GameSummary> data) {
        if (gamesPlayed(data) > 0) {
            return (double) gamesWon(data) / gamesPlayed(data);
        } else {
            return 0;
        }
    }

    private double meanTimeWin(List<GameSummary> data) {
        if (gamesPlayed(data) == 0) {
            return 0;
        }
        long total = 0;
        int count = 0;
        for (GameSummary gameSummary : data) {
            if (gameSummary.getStatus() == Status.WIN) {
                total += gameSummary.getElapsedMillis();
                count++;
            }
        }
        if (count == 0) {
            return 0;
        }
        return (double) total / (1000 * count);
    }

    private double meanTimeLose(List<GameSummary> data) {
        if (gamesPlayed(data) == 0) {
            return 0;
        }
        long total = 0;
        int count = 0;
        for (GameSummary gameSummary : data) {
            if (gameSummary.getStatus() == Status.LOSE) {
                total += gameSummary.getElapsedMillis();
                count++;
            }
        }
        if (count == 0) {
            return 0;
        }
        return (double) total / (1000 * count);
    }
}
