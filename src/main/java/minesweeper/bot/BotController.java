package src.main.java.minesweeper.bot;

// import java.util.List;

import src.main.java.minesweeper.logic.Minesweeper;
import src.main.java.minesweeper.logic.Status;
import src.main.java.minesweeper.logic.Tile;

/**
 * Controller class to run a bot on a Minesweeper Tile implementation. Similar idea to an Iterator.
 */
public class BotController {

    private Minesweeper minesweeper;
    private Bot<Tile> bot;

    public BotController(Minesweeper minesweeper, Bot<Tile> bot) {
        this.minesweeper = minesweeper;
        this.bot = bot;
    }

    /**
     * Returns true if the bot can make a next move.
     * @param minesweeper the Minesweeper game to play on
     * @param bot the bot to use on the minesweeper game
     */
    public boolean hasNext() {
        return minesweeper.status() == Status.PLAYING;
    }

    /**
     * Method that flags and clears the appropriate tiles. Performs the next move.

     */
    public void next() {
        if (hasNext()) {
            Tile tileToClear = bot.tileToClear();
            // List<Tile> tilesToFlag = bot.tilesToFlag();
            // for (Tile tile : tilesToFlag) {
            //     tile.setFlag(true);
            // }
            minesweeper.clear(tileToClear.getRow(), tileToClear.getColumn());
        }
    }
}
