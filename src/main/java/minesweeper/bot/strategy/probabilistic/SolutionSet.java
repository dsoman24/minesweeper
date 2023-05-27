package src.main.java.minesweeper.bot.strategy.probabilistic;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import src.main.java.minesweeper.logic.MinesweeperTileable;

/**
 * Stores a list of all solutions (ResultNodes) for a game state.
 */
public class SolutionSet<T extends MinesweeperTileable> {
    private List<ResultNode<T>> resultNodes;

    /**
     * Initializes an empty solution set.
     */
    public SolutionSet() {
        resultNodes = new ArrayList<>();
    }

    /**
     * Add a new round of solutions
     */
    public void addResultNode(ResultNode<T> results) {
        resultNodes.add(results);
    }

    /**
     * @param index the index of the result node we want to see
     * @param tileSet the TileSet we want to get the alpha of
     * @return the alpha of the the given TileSet
     */
    public Integer getNumMinesAtSpecificResultNode(int index, TileSet<T> tileSet) {
        return resultNodes.get(index).get(tileSet);
    }

    /**
     * @return the number of result nodes within this solution set i.e. the size of the solution set.
     */
    public int getNumberOfResultNodes() {
        return resultNodes.size();
    }

    /**
     * @return a list of the number of mine configurations per solution.
     */
    public List<BigInteger> numCombinationsPerSolution() {
        List<BigInteger> result = new ArrayList<>();
        for (ResultNode<T> resultNode : resultNodes) {
            result.add(resultNode.numCombinations());
        }
        return result;
    }

    /**
     * Sum of all combinations across all possible solutions.
     * @return the sum of of combinations across all result nodes.
     */
    public BigInteger totalNumCombinations() {
        BigInteger sum = BigInteger.ZERO;
        List<BigInteger> result = numCombinationsPerSolution();
        for (BigInteger i : result) {
            sum = sum.add(i);
        }
        return sum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (ResultNode<T> resultNode : resultNodes) {
            sb.append("Solution " + i + ":");
            sb.append(resultNode.toString());
            sb.append("\n");
            i++;
        }
        sb.append("---");
        return sb.toString();
    }
}
