package src.main.java.minesweeper.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;

public class SpriteSet {

    private static final String BASE_PATH = "img/sprites/tiles";
    private static final String PREFIX = "sprite_";
    public static final int SIZE = 30;
    private final String path;

    private static enum Type {
        ZERO("00"),
        ONE("01"),
        TWO("02"),
        THREE("03"),
        FOUR("04"),
        FIVE("05"),
        SIX("06"),
        SEVEN("07"),
        EIGHT("08"),
        UNCLEARED("09"),
        FLAG("10"),
        FLAG_INCORRECT("11"),
        MINE("12"),
        MINE_TRIGGERED("13");

        private String fileName;

        private Type(String spriteId) {
            this.fileName = PREFIX + spriteId;
        }
    }

    private ImagePattern zeroSprite;
    private ImagePattern oneSprite;
    private ImagePattern twoSprite;
    private ImagePattern threeSprite;
    private ImagePattern fourSprite;
    private ImagePattern fiveSprite;
    private ImagePattern sixSprite;
    private ImagePattern sevenSprite;
    private ImagePattern eightSprite;
    private ImagePattern unclearedSprite;
    private ImagePattern flagSprite;
    private ImagePattern flagIncorrectSprite;
    private ImagePattern mineSprite;
    private ImagePattern mineTriggeredSprite;

    public SpriteSet(SpriteStyle style) {
        path = BASE_PATH + "/" + style.name().toLowerCase();

        zeroSprite = generateImagePattern(Type.ZERO);
        oneSprite = generateImagePattern(Type.ONE);
        twoSprite = generateImagePattern(Type.TWO);
        threeSprite = generateImagePattern(Type.THREE);
        fourSprite = generateImagePattern(Type.FOUR);
        fiveSprite = generateImagePattern(Type.FIVE);
        sixSprite = generateImagePattern(Type.SIX);
        sevenSprite = generateImagePattern(Type.SEVEN);
        eightSprite = generateImagePattern(Type.EIGHT);
        unclearedSprite = generateImagePattern(Type.UNCLEARED);
        flagSprite = generateImagePattern(Type.FLAG);
        flagIncorrectSprite = generateImagePattern(Type.FLAG_INCORRECT);
        mineSprite = generateImagePattern(Type.MINE);
        mineTriggeredSprite = generateImagePattern(Type.MINE_TRIGGERED);

    }

    private ImagePattern generateImagePattern(Type type) {
        Image image = new Image(String.format(path + "/%s.png", type.fileName));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(SIZE);
        imageView.setFitHeight(SIZE);
        return new ImagePattern(imageView.snapshot(null, null));
    }

    public ImagePattern getNumberSprite(int number) {
        switch (number) {
            case 0: return zeroSprite;
            case 1: return oneSprite;
            case 2: return twoSprite;
            case 3: return threeSprite;
            case 4: return fourSprite;
            case 5: return fiveSprite;
            case 6: return sixSprite;
            case 7: return sevenSprite;
            default: return eightSprite;
        }
    }

    public ImagePattern getUnclearedSprite() {
        return this.unclearedSprite;
    }

    public ImagePattern getFlagSprite() {
        return this.flagSprite;
    }

    public ImagePattern getFlagIncorrectSprite() {
        return this.flagIncorrectSprite;
    }

    public ImagePattern getMineSprite() {
        return this.mineSprite;
    }

    public ImagePattern getMineTriggeredSprite() {
        return this.mineTriggeredSprite;
    }

}
