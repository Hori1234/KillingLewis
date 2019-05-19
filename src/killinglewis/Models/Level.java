package killinglewis.Models;

import killinglewis.math.Vector3f;
import killinglewis.utils.Maze;

public class Level {
    Lewis lewis;
    Terrain terrain;
    Maze maze;

    public Level(Maze maze) {
        maze = new Maze("mazes/maze1.txt");
        lewis = new Lewis(maze.getStartX(), maze.getStartY());
        terrain = new Terrain(maze);

        lewis.moveTo(terrain.getCellPosition(lewis.getMazeX(), lewis.getMazeY()));
    }

    public void render() {
        terrain.render();
        lewis.render();
    }

    public void moveToCell(int x, int y) {
        lewis.moveTo(terrain.getCellPosition(x, y));
    }

    public void runToCell(int x, int y) {
        lewis.runTo(terrain.getCellPosition(x, y));
    }

    public Lewis getLewis() {
        return lewis;
    }

    public Terrain getTerrain() {
        return terrain;
    }
}
