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

    private MinesweeperPane minesweeperPane;

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
        ComboBox<Difficulty> difficultyInput = new ComboBox<>();
        for (Difficulty difficulty : Difficulty.values()) {
            difficultyInput.getItems().add(difficulty);
        }
        Button startButton = new Button("Start");
        startButton.setPrefHeight(45);

        difficultyVBox.getChildren().addAll(difficultyLabel, difficultyInput);

        inputPane.setAlignment(Pos.CENTER);

        startButton.setOnAction(e -> {

            Stage gameStage = new Stage();

            VBox subroot = new VBox();
            HBox gameDetails = new HBox();

            Difficulty difficulty = difficultyInput.getValue();
            if (difficulty == null) {
                difficulty = Difficulty.EASY;
            }
            minesweeperPane = new MinesweeperPane(difficulty, gameStage);

            minesweeperPane.setAlignment(Pos.CENTER);
            gameDetails.setAlignment(Pos.CENTER);
            gameDetails.setSpacing(20);
            subroot.setPadding(new Insets(10));
            subroot.setSpacing(10);
            gameDetails.getChildren().add(minesweeperPane.getFlagLabel());
            gameDetails.getChildren().add(minesweeperPane.getSecondsLabel());
            subroot.getChildren().add(gameDetails);
            subroot.getChildren().add(minesweeperPane);

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
            Difficulty difficulty = difficultyInput.getValue();
            if (difficulty == null) {
                difficulty = Difficulty.EASY;
            }

            Label leaderboardLabel = new Label(String.format("%s | Leaderboard", difficulty));
            TableView<LeaderboardEntry> table = new TableView<>();
            table.setPlaceholder(new Label("No entries for this difficulty yet!"));
            TableColumn<LeaderboardEntry, String> dateColumn = new TableColumn<>("Date");
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            TableColumn<LeaderboardEntry, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            TableColumn<LeaderboardEntry, String> diffColumn = new TableColumn<>("Difficulty");
            diffColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
            TableColumn<LeaderboardEntry, Double> timeColumn = new TableColumn<>("Time");
            timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

            table.getColumns().addAll(dateColumn, nameColumn, diffColumn, timeColumn);

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
}
