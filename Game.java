import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;

/**
 * Application class
 */
public class Game extends Application {

    private Minesweeper minesweeper;

    @Override
    @SuppressWarnings("unchecked")
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
        ComboBox<String> difficultyInput = new ComboBox<>();
        difficultyInput.getItems().addAll(
            "Easy (9x9, 10 mines)", "Medium (16x16, 40 mines)", "Hard (16x30, 99 mines)", "Expert (24x30, 180 mines)"
        );
        Button startButton = new Button("Start");
        startButton.setPrefHeight(45);

        difficultyVBox.getChildren().addAll(difficultyLabel, difficultyInput);

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

        Button showLeaderboard = new Button("Show Leaderboard");
        showLeaderboard.setPrefHeight(45);

        showLeaderboard.setOnAction(e -> {
            Stage leaderboardStage = new Stage();
            VBox leaderboardRoot = new VBox();
            leaderboardRoot.setAlignment(Pos.CENTER);
            String difficulty = difficultyInput.getValue();
            if (difficulty == null) {
                difficulty = "Easy (9x9, 10 mines)";
            }

            Label leaderboardLabel = new Label(String.format("%s | Leaderboard", difficulty));
            TableView<LeaderboardEntry> table = new TableView<>();
            table.setPlaceholder(new Label("No entries for this difficulty yet!"));
            TableColumn<LeaderboardEntry, String> dateColumn = new TableColumn<>("Date");
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            TableColumn<LeaderboardEntry, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            TableColumn<LeaderboardEntry, Double> timeColumn = new TableColumn<>("Time");
            timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

            table.getColumns().addAll(dateColumn, nameColumn, timeColumn);

            File file = new File("leaderboard.csv");
            List<LeaderboardEntry> entries = new ArrayList<>();
            try {
                Scanner scan = new Scanner(file);
                scan.nextLine();
                while (scan.hasNextLine()) {
                    LeaderboardEntry entry = LeaderboardEntry.parseLine(scan.nextLine());
                    if (difficulty.equals(entry.getDifficulty())) {
                        entries.add(entry);
                    }
                }
                scan.close();
            } catch (FileNotFoundException fnfe) {
                System.out.println("File not found");
            }

            Collections.sort(entries);
            ObservableList<LeaderboardEntry> list = FXCollections.observableArrayList(entries);
            table.setItems(list);
            leaderboardRoot.getChildren().addAll(leaderboardLabel, table);

            Scene leaderScene = new Scene(leaderboardRoot);
            leaderboardStage.setScene(leaderScene);
            leaderboardStage.show();
        });

        inputPane.getChildren().addAll(difficultyVBox, startButton, showLeaderboard);
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
