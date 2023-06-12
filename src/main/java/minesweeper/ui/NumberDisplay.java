package src.main.java.minesweeper.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class NumberDisplay {

    private static final int HEIGHT = 30;
    private static final int WIDTH = 16;
    private final int characters;
    private final int maxValue;

    private HBox root;

    private static enum NumberSprite {
        NONE("00"),
        ZERO("01"),
        ONE("02"),
        TWO("03"),
        THREE("04"),
        FOUR("05"),
        FIVE("06"),
        SIX("07"),
        SEVEN("08"),
        EIGHT("09"),
        NINE("10");

        private static final String BASE_PATH = "img/sprites/misc/numbers";
        private static final String PREFIX = "sprite_";

        private ImagePattern sprite;

        private NumberSprite(String spriteId) {
            Image image = new Image(String.format(BASE_PATH + "/%s%s.png", PREFIX, spriteId));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(WIDTH);
            imageView.setFitHeight(HEIGHT);
            sprite = new ImagePattern(imageView.snapshot(null, null));
        }

        private ImagePattern getSprite() {
            return sprite;
        }

        private static ImagePattern getSpriteForNumber(int number) {
            return values()[number + 1].getSprite();
        }
    }

    public NumberDisplay(int characters, int initialValue) {
        this.characters = characters;
        this.root = new HBox();
        // initialize the rectangles (and maxValue)
        String maxValueString = "";
        for (int i = 0; i < characters; i++) {
            Rectangle rectangle = new Rectangle(WIDTH, HEIGHT);
            root.getChildren().add(rectangle);
            maxValueString += "9";
        }
        maxValue = Integer.valueOf(maxValueString);

        update(initialValue);
    }

    public void update(int newValue) {
        if (newValue <= maxValue) {
            String string = String.valueOf(newValue);

            for (int i = 0; i < characters - string.length(); i++) {
                ((Rectangle)(root.getChildren().get(i))).setFill(NumberSprite.getSpriteForNumber(0));
            }
            for (int i = characters - string.length(); i < characters; i++) {
                char character = string.charAt(i - (characters - string.length()));
                int number = Character.getNumericValue(character);
                ((Rectangle)(root.getChildren().get(i))).setFill(NumberSprite.getSpriteForNumber(number));
            }
        }
    }

    public HBox getRoot() {
        return root;
    }
}
