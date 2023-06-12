package src.main.java.minesweeper.ui;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import src.main.java.minesweeper.logic.Difficulty;

public class GameStage extends Stage {

    private final VBox root;
    private final Scene scene;

    public GameStage(Difficulty difficulty) {
        root = new VBox();
        scene = new Scene(root);
        initialize(difficulty);
    }

    public void setStyle(SpriteStyle style) {
        BackgroundFill updatedFill = new BackgroundFill(style.getBackgroundColor(), null, null);
        Background updatedBackground = new Background(updatedFill);
        root.setBackground(updatedBackground);
    }

    private void initialize(Difficulty difficulty) {
        MinesweeperPane minesweeperPane = new MinesweeperPane(difficulty, this);
        root.getChildren().add(new SettingsBar(this, minesweeperPane));
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

        root.setSpacing(10);
        gameDetails.getChildren().add(minesweeperPane.getFlagDisplay().getRoot());

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(e -> {
            clearRoot();
            initialize(difficulty);
        });
        gameDetails.getChildren().add(resetButton);

        gameDetails.getChildren().add(minesweeperPane.getTimerDisplay().getRoot());
        gameDetails.setSpacing(20);

        botDetails.getChildren().add(minesweeperPane.getBotInput());
        root.getChildren().add(gameDetails);
        root.getChildren().add(minesweeperPane);
        root.getChildren().add(botDetails);

        setTitle(String.format("Minesweeper %s", difficulty.toString()));

        setStyle(SpriteStyle.DEFAULT_STYLE);
        setScene(scene);
    }

    private void clearRoot() {
        root.getChildren().clear();
    }
}
