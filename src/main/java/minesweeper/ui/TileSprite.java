package src.main.java.minesweeper.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;

public enum TileSprite {
    ZERO("sprite_00"),
    ONE("sprite_01"),
    TWO("sprite_02"),
    THREE("sprite_03"),
    FOUR("sprite_04"),
    FIVE("sprite_05"),
    SIX("sprite_06"),
    SEVEN("sprite_07"),
    EIGHT("sprite_08"),
    UNCLEARED("sprite_09"),
    FLAG("sprite_10"),
    FLAG_INCORRECT("sprite_11"),
    MINE("sprite_12"),
    MINE_TRIGGERED("sprite_13");

    public static final int SIZE = 30;

    private ImagePattern imagePattern;

    private TileSprite(String spriteName) {
        Image image = new Image(String.format("img/sprites/%s.png", spriteName));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(SIZE);
        imageView.setFitHeight(SIZE);
        imagePattern = new ImagePattern(imageView.snapshot(null, null));
    }

    public ImagePattern getImagePattern() {
        return imagePattern;
    }

    public static TileSprite getClearedNumberedSprite(int number) {
        return values()[number];
    }
}
