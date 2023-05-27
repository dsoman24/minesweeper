package src.main.java.minesweeper.logic;

/**
 * Interface for a Minesweeper-tileable object
 */
public interface Tileable {

    int getNumberOfNeighboringMines();

    int getRow();

    int getColumn();

    boolean isCleared();

    boolean isFlagged();

    double initialDensity();

}
