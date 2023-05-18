package src.game_gui;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.minesweeper.Difficulty;
import src.minesweeper.leaderboard.LeaderboardStage;
import javafx.geometry.Insets;

/**
 * Application class
 */
public class Game extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Minesweeper");
        primaryStage.setMinHeight(100);
        primaryStage.setMaxHeight(100);
        primaryStage.setMaxWidth(500);
        primaryStage.setMinWidth(500);

        VBox root = new VBox();
        HBox inputPane = new HBox();
        VBox difficultyVBox = new VBox();
        inputPane.setSpacing(10);
        inputPane.setPadding(new Insets(10));
        root.getChildren().add(inputPane);
        root.setAlignment(Pos.CENTER);

        Label difficultyLabel = new Label("Difficulty: ");
        ComboBox<Difficulty> difficultyInput = new ComboBox<>();
        for (Difficulty difficulty : Difficulty.values()) {
            difficultyInput.getItems().add(difficulty);
        }
        Button startButton = new Button("Start");
        startButton.setPrefHeight(45);

        difficultyVBox.getChildren().addAll(difficultyLabel, difficultyInput);

        inputPane.setAlignment(Pos.CENTER);

        startButton.setOnAction(e -> {

            Difficulty difficulty = difficultyInput.getValue();
            if (difficulty == null) {
                difficulty = Difficulty.EASY;
            }

            GameStage gameStage = new GameStage(difficulty);

            gameStage.show();
            primaryStage.hide();
            gameStage.setOnHidden(a -> primaryStage.show());
        });

        Button showLeaderboard = new Button("Show Leaderboard");
        showLeaderboard.setPrefHeight(45);

        showLeaderboard.setOnAction(e -> {
            Difficulty difficulty = difficultyInput.getValue();
            LeaderboardStage leaderboardStage = new LeaderboardStage(difficulty);
            leaderboardStage.show();
        });

        inputPane.getChildren().addAll(difficultyVBox, startButton, showLeaderboard);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
