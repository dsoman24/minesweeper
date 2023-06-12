package src.main.java.minesweeper.ui;

import javafx.scene.paint.Color;

public enum SpriteStyle {
    CLASSIC(Color.WHITE),
    DARK(Color.web("#787878"));

    private SpriteSet spriteSet;
    private Color backgroundColor;

    public static final SpriteStyle DEFAULT_STYLE = SpriteStyle.CLASSIC;

    private SpriteStyle(Color backgroundColor) {
        spriteSet = new SpriteSet(this);
        this.backgroundColor = backgroundColor;
    }

    public SpriteSet getSpriteSet() {
        return spriteSet;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }
}
