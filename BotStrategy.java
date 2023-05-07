/**
 * Strategy interface.
 */
public interface BotStrategy {

    /**
     * Implementing interfaces define how this strategy performs a move.
     */
    Status move(Minesweeper minesweeper);
}
