# minesweeper

## Description

Minesweeper game with GUI supported by JavaFX. Notable feature is different bot strategies:

1. Linear
    - Clears tiles one by one and is guaranteed to hit a mine. This will always lose.
2. Random
    - Picks a random tile to clear each turn.
3. Probabilistic
    - On each turn, computes the probability that each tile in a game state has a mine, and clears the least likely tile.
    - It does not win 100% of the time!

## TODO:
- [ ] Better write up, with diagrams.
- [ ] Write up how to describe computations.
- [ ] Performance analysis.

## Probabilistic Algorithm:

1. The algorithm first groups uncleared tiles into TileSets. A TileSet is a set of Tiles such that each Tile within the set share the same common neighboring cleared (non-zero numbered) tiles.
    - Consequently, a minesweeper game state will have one unique partition into TileSets

2. The algorithm then collects all TileSetRules.
    - TileSetRules are essentially linear equations. Each non-zero numbered and cleared tile will correspond to a rule. Each variable within a rule is a TileSet. For example, if we have a cleared tile numbered 3 (that is, in the eight surrounding tiles, three of them are mines), and this tile has the TileSets $A$, $B$, $C$, and $D$ surrounding it, we have the rule $$A + B + C + D = 3$$
    - Rules can be simplified:
        - Simplifying a rule involves substitution in known values for variables and removing these from the rule. For instance, if we have a rule $$A + B + C = 2$$ and we know that $A = 1$, then clearly, $B + C = 2$.
        - There are three base cases that one can hit after simplifying a rule:
            1. There is one variable remaining.
                - If we end up with a rule with only one TileSet e.g. $D = 3$, then clearly, the TileSet $D$ must have 3 mines within it.
            2. The result of the rule is 0.
                - If we end up with a rule like $A + B + C = 0$, we can easily assign the number of mines within TileSets $A$, $B$, and $C$ to be $0$.
            3. The result is equal to the number of Tiles within the TileSets in the rule.
                - For instance, if we have TileSets $A = \{a, b, c\}$ and $B = \{d, e\}$, and the rule $A + B = 4$, then we can assign a value of $3$ to $A$ and a value of $2$ to $B$.
    - Note that this includes a Global rule to account for the total number of mines in the board.
3. The algorithm then uses a form of pre-order traversal on a solution tree to count the number of solutions in a GameState.
    - The solution tree is composed of ResultNodes, each of which contain their own simplified TileSetRule set, and a running solution, aka supposed results.
    - A supposed result is a mapping of TileSets to the number of mines within each tile set
    - The following are the steps of building the solution tree to populate the final supposed results:
        1. Create the root with an empty supposed result (each TileSet maps to null) and the original, unsimplified rules.
        2. Select the smallest unknown TileSet, $S$, within the supposed result.
        3. Assign values in the range $[0, |S|]$ to the supposed result, where $|S|$ is the cardinality of $S$. Attempt to simplify each rule.
            - If the result of a simplification is ever invalid, this branch of the solution tree is no longer traveresed.
            - If the result of the simplification is valid, we recursively continue.
        4. If the supposed result is fully populated, that is, we know a value for each TileSet, we stop and add this to the known final results. At this point, we have reached a leaf node in the tree. Otherwise, we pick the next smallest unknown element and repeat from step 2.
4. With the solution tree built, we can begin computing probabilities.
    - <b>Write up how to do this</b>
5. Pick the least likely TileSet. This is the TileSet with the lowest probability of containing a mine. Then, randomly pick a Tile within this TileSet, and clear it. The game state is now updated, and we repeat from the start until the game ends (either a win or a loss).


## Some sources:

Main algo inspriation: https://codereview.stackexchange.com/questions/54737/analyzing-minesweeper-probabilities

https://dash.harvard.edu/bitstream/handle/1/14398552/BECERRA-SENIORTHESIS-2015.pdf

https://www.claymath.org/sites/default/files/minesweeper.pdf

https://minesweepergame.com/math/explorations-of-the-minesweeper-consistency-problem-2003.pdf

https://www.minesweeper.info/articles/SomeMinesweeperConfigurations.pdf

https://web.mat.bham.ac.uk/R.W.Kaye/minesw/infmsw.pdf

https://www.youtube.com/watch?v=G2kd745uYuo&ab_channel=CodingChannel

## JavaFX and GUI


Compile with: javac --module-path /path/javafx-sdk-11.0.2/lib --add-modules javafx.controls *.java

Run with: java --module-path /path/javafx-sdk-11.0.2/lib --add-modules javafx.controls Game

Uses a simple file-based leaderboard system, saving the results into a local csv file in the same directory as the game.
