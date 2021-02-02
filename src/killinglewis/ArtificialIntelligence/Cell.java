package killinglewis.ArtificialIntelligence;

/**
 * @author ralucaviziteu
 */

//we define a cell of our maze
public class Cell {

    //coordinates
    public int i, j;
    //parent cell for path
    public Cell parent;
    //Heuristic cost of the current cell
    public int heuristicCost;
    //Final cost
    public int finalCost; // f(x) = g(x) + h(x)
    public boolean solution; // true if the cell is part of the solution path

    public Cell(int i, int j) {
        this.i = i;
        this.j = j;
    }
}
