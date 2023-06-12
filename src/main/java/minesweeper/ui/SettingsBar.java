package src.main.java.minesweeper.ui;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class SettingsBar extends MenuBar {

    private MinesweeperPane minesweeperPane;

    public SettingsBar(MinesweeperPane minesweeperPane) {
        this.minesweeperPane = minesweeperPane;

        Menu settingsMenu = new Menu("Style");
        for (SpriteStyle style : SpriteStyle.values()) {
            settingsMenu.getItems().add(new StyleItem(style));
        }

        getMenus().add(settingsMenu);
    }

    private class StyleItem extends MenuItem {

        private SpriteStyle style;

        public StyleItem(SpriteStyle style) {
            super(style.name());
            this.style = style;

            setOnAction(e -> {
                changeStyle();
            });
        }

        public void changeStyle() {
            minesweeperPane.setSpriteStyle(style);
            minesweeperPane.update();
        }
    }
}
