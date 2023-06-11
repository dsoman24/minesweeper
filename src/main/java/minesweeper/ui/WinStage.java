package src.main.java.minesweeper.ui;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.main.java.minesweeper.logic.Difficulty;
import src.main.java.minesweeper.ui.leaderboard.LeaderboardEntry;
import src.main.java.minesweeper.ui.leaderboard.LeaderboardIO;

public class WinStage extends Stage {

    public WinStage(Timer timer, Stage gameStage, Difficulty difficulty, boolean eligibleToBeSaved) {
        setTitle("You Win!");

        VBox root = new VBox();

        Label winLabel = new Label(
            String.format("[%d.%d s]",
            timer.getElapsedSeconds(), timer.getElapsedMilliseconds())
        );

        Label promptLabel = new Label("Enter your name to enter the leaderboard");

        TextField nameField = new TextField();

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(a -> {
            close();
            gameStage.close();
        });

        Button saveToLeaderboard = new Button("Submit");
        saveToLeaderboard.setOnAction(e -> {
            String name = nameField.getText();
            double time = Double.parseDouble(
                String.format("%d.%d", timer.getElapsedSeconds(), timer.getElapsedMilliseconds())
            );

            LeaderboardEntry entry = new LeaderboardEntry(name, difficulty, time);

            LeaderboardIO.write("leaderboard.csv", entry);

            close();
            gameStage.close();
        });

        root.getChildren().add(winLabel);
        if (eligibleToBeSaved) {
            root.getChildren().addAll(promptLabel, nameField, saveToLeaderboard);
        }
        root.getChildren().add(exitButton);

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));
        root.setSpacing(10);
        Scene scene = new Scene(root);

        setScene(scene);
    }
}
