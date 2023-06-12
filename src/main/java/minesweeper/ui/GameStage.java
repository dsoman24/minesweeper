package src.main.java.minesweeper.ui;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import src.main.java.minesweeper.logic.Difficulty;

public class GameStage extends Stage {

    public GameStage(Difficulty difficulty) {
        MinesweeperPane minesweeperPane = new MinesweeperPane(difficulty, this);

        VBox subroot = new VBox(new SettingsBar(minesweeperPane));
        HBox gameDetails = new HBox();
        HBox botDetails = new HBox();


        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        setX(bounds.getMinX());
        setY(bounds.getMinY());
        setWidth(bounds.getWidth());
        setHeight(bounds.getHeight());

        gameDetails.setAlignment(Pos.CENTER);
        gameDetails.setSpacing(20);

        botDetails.setAlignment(Pos.CENTER);

        subroot.setPadding(new Insets(10));
        subroot.setSpacing(10);
        gameDetails.getChildren().add(minesweeperPane.getFlagLabel());
        gameDetails.getChildren().add(minesweeperPane.getSecondsLabel());
        botDetails.getChildren().add(minesweeperPane.getBotInput());
        subroot.getChildren().add(gameDetails);
        subroot.getChildren().add(minesweeperPane);
        subroot.getChildren().add(botDetails);

        setTitle(String.format("Minesweeper %s", difficulty.toString()));

        Scene scene = new Scene(subroot);
        setScene(scene);
    }

}
