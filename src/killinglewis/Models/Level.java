package killinglewis.Models;

import killinglewis.ArtificialIntelligence.AStar;
import killinglewis.math.Vector3f;
import killinglewis.utils.Maze;

import java.util.ArrayList;

public class Level {
    Lewis lewis;
    Terrain terrain;
    Maze maze;
    ArrayList<Integer> path;
    Overlay health, mana;

    public Level(Maze maze) {
        this.maze = maze;
        lewis = new Lewis(maze.getStartX(), maze.getStartY());
        terrain = new Terrain(maze);
        health = new Overlay("textures/health.png", 0);
        mana = new Overlay("textures/mana.png", 1);

        lewis.moveTo(terrain.getCellPosition(lewis.getMazeX(), lewis.getMazeY()));
    }

    public void render() {
        terrain.render();
        lewis.render();
        health.render();
        mana.render();
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

    public void findPath() {
        path = AStar.getSolutionPath(maze);
    }

    public void runToNextCell() {
        Vector3f next = getNextCell();
        runToCell((int) next.getX(), (int) next.getY());
    }

    private Vector3f getNextCell() {
        if (!path.isEmpty()) {
            Vector3f next = new Vector3f(path.get(path.size() - 1), path.get(path.size() - 2), 0);
            path.remove(path.size() - 1);
            path.remove(path.size() - 1);
            return next;
        } else {
            return new Vector3f(maze.getGoalY(), maze.getGoalX(), 0);
        }
    }
}
