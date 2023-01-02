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
        primaryStage.setMinHeight(100);
        primaryStage.setMaxHeight(100);
        primaryStage.setMaxWidth(300);
        primaryStage.setMinWidth(300);

        VBox root = new VBox();
        HBox inputPane = new HBox();
        VBox difficultyVBox = new VBox();
        inputPane.setSpacing(10);
        inputPane.setPadding(new Insets(10));
        root.getChildren().add(inputPane);
        root.setAlignment(Pos.CENTER);

        Label difficultyLabel = new Label("Difficulty: ");
        ComboBox<String> difficultyInput = new ComboBox<>();
        difficultyInput.getItems().addAll(
            "Easy (9x9, 10 mines)", "Medium (16x16, 40 mines)", "Hard (16x30, 99 mines)", "Expert (24x30, 180 mines)"
        );
        Button startButton = new Button("Start");
        startButton.setPrefHeight(45);

        difficultyVBox.getChildren().addAll(difficultyLabel, difficultyInput);

        inputPane.getChildren().addAll(difficultyVBox, startButton);
        inputPane.setAlignment(Pos.CENTER);

        startButton.setOnAction(e -> {

            Stage gameStage = new Stage();

            VBox subroot = new VBox();
            HBox gameDetails = new HBox();

            String difficulty = difficultyInput.getValue();
            if (difficulty == null || difficulty.equals("Easy (9x9, 10 mines)")) {
                minesweeper = new Minesweeper(9, 9, 10, gameStage);
                gameStage.setTitle("Minesweeper Easy (9x9, 10 mines)");
                setStageSize(gameStage, 9, 9);
            } else if (difficulty == "Medium (16x16, 40 mines)") {
                minesweeper = new Minesweeper(16, 16, 40, gameStage);
                gameStage.setTitle("Minesweeper Medium (16x16, 40 mines");
                setStageSize(gameStage, 16, 16);
            } else if (difficulty == "Hard (16x30, 99 mines)") {
                minesweeper = new Minesweeper(16, 30, 99, gameStage);
                gameStage.setTitle("Minesweeper Hard (16x30, 99 mines)");
                setStageSize(gameStage, 16, 30);
            } else if (difficulty == "Expert (24x30, 180 mines)") {
                minesweeper = new Minesweeper(24, 30, 180, gameStage);
                gameStage.setTitle("Minesweeper Expert (24x30, 180 mines)");
                setStageSize(gameStage, 24, 30);
            }

            minesweeper.setAlignment(Pos.CENTER);
            gameDetails.setAlignment(Pos.CENTER);
            gameDetails.setSpacing(20);
            subroot.setPadding(new Insets(10));
            subroot.setSpacing(10);
            gameDetails.getChildren().add(minesweeper.getFlagLabel());
            gameDetails.getChildren().add(minesweeper.getSecondsLabel());
            subroot.getChildren().add(gameDetails);
            subroot.getChildren().add(minesweeper);

            Scene scene = new Scene(subroot);
            gameStage.setScene(scene);
            gameStage.show();
            primaryStage.hide();
            gameStage.setOnHidden(a -> primaryStage.show());
        });

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * primaryStage size setter helper method.
     * @param primaryStage the primary stage to set the size of
     * @param rows the number of rows
     * @param columns the number of columns
     */
    private static void setStageSize(Stage primaryStage, int rows, int columns) {
        primaryStage.setHeight(30 * rows + 120);
        primaryStage.setWidth(30 * columns + 100);
    }
}
