import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class Game extends Application {


    private Minesweeper minesweeper;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Minesweeper");
        VBox root = new VBox();
        HBox inputPane = new HBox();
        VBox difficultyVBox = new VBox();
        inputPane.setSpacing(10);
        inputPane.setPadding(new Insets(10));
        minesweeper = new Minesweeper(9, 9, 10);
        root.getChildren().add(inputPane);
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(minesweeper);

        primaryStage.setHeight(30 * 9 + 100);
        primaryStage.setWidth(30 * 9 + 60);

        Label difficultyLabel = new Label("Difficulty: ");
        ComboBox<String> difficultyInput = new ComboBox<>();
        difficultyInput.getItems().addAll("Easy", "Medium", "Hard", "Expert");
        Button startButton = new Button("Start Game");
        startButton.setPrefHeight(50);

        difficultyVBox.getChildren().addAll(difficultyLabel, difficultyInput);

        inputPane.getChildren().addAll(difficultyVBox, startButton);
        inputPane.getChildren().add(minesweeper.getFlagLabel());
        inputPane.setAlignment(Pos.CENTER);
        startButton.setOnAction(e -> {
            String difficulty = difficultyInput.getValue();
            root.getChildren().remove(root.getChildren().size() - 1);
            inputPane.getChildren().remove(inputPane.getChildren().size() - 1);
            if (difficulty == null || difficulty.equals("Easy")) {
                minesweeper = new Minesweeper(9, 9, 10);
                primaryStage.setHeight(30 * 9 + 100);
                primaryStage.setWidth(30 * 9 + 60);
            } else if (difficulty == "Medium") {
                minesweeper = new Minesweeper(16, 16, 40);
                primaryStage.setHeight(30 * 16 + 100);
                primaryStage.setWidth(30 * 16 + 60);
            } else if (difficulty == "Hard") {
                minesweeper = new Minesweeper(16, 30, 99);
                primaryStage.setHeight(30 * 16 + 100);
                primaryStage.setWidth(30 * 30 + 60);
            } else if (difficulty == "Expert") {
                minesweeper = new Minesweeper(24, 30, 180);
                primaryStage.setHeight(30 * 24 + 100);
                primaryStage.setWidth(30 * 30 + 60);
            }
            root.getChildren().add(minesweeper);
            inputPane.getChildren().add(minesweeper.getFlagLabel());
        });

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
