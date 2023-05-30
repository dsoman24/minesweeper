package src.main.java.minesweeper.ui;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.main.java.minesweeper.logic.Difficulty;

public class GameStage extends Stage {

    public GameStage(Difficulty difficulty) {
        VBox subroot = new VBox();
        HBox gameDetails = new HBox();

        MinesweeperPane minesweeperPane = new MinesweeperPane(difficulty, this);

        setHeight(30 * difficulty.getNumberOfRows() + 120);
        setWidth(30 * difficulty.getNumberOfColumns() + 600);

        gameDetails.setAlignment(Pos.CENTER);
        gameDetails.setSpacing(20);
        subroot.setPadding(new Insets(10));
        subroot.setSpacing(10);
        gameDetails.getChildren().add(minesweeperPane.getFlagLabel());
        gameDetails.getChildren().add(minesweeperPane.getSecondsLabel());
        gameDetails.getChildren().add(minesweeperPane.getBotInput());
        subroot.getChildren().add(gameDetails);
        subroot.getChildren().add(minesweeperPane);

        setTitle(String.format("Minesweeper %s", difficulty.toString()));

        Scene scene = new Scene(subroot);
        setScene(scene);
    }

}
