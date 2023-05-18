package src.game_gui;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoseStage extends Stage {

    public LoseStage(Stage gameStage) {
        setTitle("You Lose");
        VBox root = new VBox();

        Label loseLabel = new Label("You Lose");

        Button exitButton = new Button("Exit");

        exitButton.setOnAction(a -> {
            close();
            gameStage.close();
        });

        root.getChildren().addAll(loseLabel, exitButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));
        root.setSpacing(10);
        Scene scene = new Scene(root);
        setScene(scene);
        setWidth(200);
    }
}
