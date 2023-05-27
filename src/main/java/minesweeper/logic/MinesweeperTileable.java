package src.main.java.minesweeper.logic;

/**
 * Interface for a Minesweeper-tileable object.
 * Basic implementation requires these methods.
 * That is, for a minesweeper implementation, you need to know:
 * <ul>
 * <li> the number of neighboring mines
 * <li> the row the tile is on
 * <li> the column the tile is on
 * <li> if the tile is cleared
 * <li> if the tile is flagged
 * <li> the initial probability that this tile is a mine
 * </ul>
 */
public interface MinesweeperTileable {

    /**
     * A minesweeper game implementation needs to know the number of neighboring mines of a given tile
     * @return the number of neighboring mines
     */
    int getNumberOfNeighboringMines();

    /**
     * @return the row of the tile
     */
    int getRow();

    /**
     * @return the column of the tile
     */
    int getColumn();

    /**
     * @return true if the tile is cleared, false otherwise
     */
    boolean isCleared();

    /**
     * return true if the tile is flagged, false otherwise
     */
    boolean isFlagged();

    /**
     * Returns the initial density, e.g. the probability that this tile has a mine.
     * Initially, this is (total # mines) / (rows * columns).
     * @return the initial density
     */
    double initialDensity();

}
