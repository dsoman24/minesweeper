package src.ui;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.minesweeper.Difficulty;
import src.minesweeper.leaderboard.LeaderboardEntry;
import src.minesweeper.leaderboard.LeaderboardIO;

public class WinStage extends Stage {

    public WinStage(Timer timer, Stage gameStage, Difficulty difficulty) {
        setTitle("You Win!");

        VBox root = new VBox();

        Label winLabel = new Label(
            String.format("[%d.%d s] Enter your name to enter the leaderboard",
            timer.getElapsedSeconds(), timer.getElapsedMilliseconds())
        );
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");

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

            LeaderboardIO.write(entry);

            close();
            gameStage.close();
        });

        root.getChildren().addAll(winLabel, nameField, saveToLeaderboard, exitButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));
        root.setSpacing(10);
        Scene scene = new Scene(root);

        setScene(scene);
    }
}
