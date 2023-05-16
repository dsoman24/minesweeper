import java.util.ArrayList;
import java.util.List;

/**
 * Stores a list of all supposed results.
 */
public class SupposedResults {
    private List<ResultNode> resultNodes;

    public SupposedResults() {
        resultNodes = new ArrayList<>();
    }

    /**
     * Add a new round of supposed results.
     */
    public void addResultNode(ResultNode results) {
        resultNodes.add(results);
    }

    public Integer getNumMinesAtSpecificResultNode(int index, TileSet tileSet) {
        return resultNodes.get(index).get(tileSet);
    }

    public void setResultNode(int index, ResultNode resultNode) {
        resultNodes.set(index, resultNode);
    }

    public int getNumberOfResultNodes() {
        return resultNodes.size();
    }

    /**
     * Returns a list of the number of mine configurations per solution
     */
    public List<Integer> numCombinationsPerSolution() {
        List<Integer> result = new ArrayList<>();
        for (ResultNode resultNode : resultNodes) {
            result.add(resultNode.numCombinations());
        }
        return result;
    }

    /**
     * Sum of all combinations across all possible solutionsß
     */
    public int totalNumCombinations() {
        int sum = 0;
        List<Integer> result = numCombinationsPerSolution();
        for (Integer i : result) {
            sum += i;
        }
        return sum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (ResultNode resultNode : resultNodes) {
            sb.append("Solution " + i + ":");
            sb.append(resultNode.toString());
            sb.append("\n");
            i++;
        }
        sb.append("---");
        return sb.toString();
    }
}
