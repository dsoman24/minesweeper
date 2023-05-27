package src.main.java.minesweeper.bot.strategy.probabilistic;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import src.main.java.minesweeper.logic.Tileable;

/**
 * Stores a list of all solutions for a game state
 */
public class SolutionSet<T extends Tileable> {
    private List<ResultNode<T>> resultNodes;

    public SolutionSet() {
        resultNodes = new ArrayList<>();
    }

    /**
     * Add a new round of solutions
     */
    public void addResultNode(ResultNode<T> results) {
        resultNodes.add(results);
    }

    public Integer getNumMinesAtSpecificResultNode(int index, TileSet<T> tileSet) {
        return resultNodes.get(index).get(tileSet);
    }

    public void setResultNode(int index, ResultNode<T> resultNode) {
        resultNodes.set(index, resultNode);
    }

    public int getNumberOfResultNodes() {
        return resultNodes.size();
    }

    /**
     * Returns a list of the number of mine configurations per solution
     */
    public List<BigInteger> numCombinationsPerSolution() {
        List<BigInteger> result = new ArrayList<>();
        for (ResultNode<T> resultNode : resultNodes) {
            result.add(resultNode.numCombinations());
        }
        return result;
    }

    /**
     * Sum of all combinations across all possible solutions
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
