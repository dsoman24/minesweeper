package src.main.java.minesweeper.ui;

public enum SpriteStyle {
    CLASSIC,
    DARK;

    private SpriteSet spriteSet;

    private SpriteStyle() {
        spriteSet = new SpriteSet(this);
    }

    public SpriteSet getSpriteSet() {
        return spriteSet;
    }
}
