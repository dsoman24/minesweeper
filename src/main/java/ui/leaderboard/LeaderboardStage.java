package src.main.java.ui.leaderboard;
import java.util.Collections;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.main.java.minesweeper.Difficulty;

public class LeaderboardStage extends Stage {

    @SuppressWarnings("unchecked")
    public LeaderboardStage(Difficulty difficulty) {
        VBox leaderboardRoot = new VBox();
        leaderboardRoot.setAlignment(Pos.CENTER);
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

        List<LeaderboardEntry> entries = LeaderboardIO.read("leaderboard.csv", difficulty);

        Collections.sort(entries);
        ObservableList<LeaderboardEntry> list = FXCollections.observableArrayList(entries);
        table.setItems(list);
        leaderboardRoot.getChildren().addAll(leaderboardLabel, table);

        Scene leaderScene = new Scene(leaderboardRoot);
        setScene(leaderScene);
    }
}
