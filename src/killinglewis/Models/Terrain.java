package killinglewis.Models;

import killinglewis.math.Vector3f;
import killinglewis.utils.Maze;
import killinglewis.utils.Shader;

import java.util.ArrayList;

import static java.lang.Math.floor;
import static killinglewis.KillingLewis.*;

public class Terrain {
    private VertexArray terrain;
    private Maze maze;
    private ArrayList<Wall> walls;
    public float cellWidth;
    public float cellHeight;

    public Terrain(Maze maze) {
        terrain = new VertexArray("res/terrain.obj", "textures/terrain.jpg", Shader.TERRAIN_SHADER);
        terrain.translate(new Vector3f(0.0f, 0.0f, -10.0f));
        terrain.scale(new Vector3f(RIGHT * 2, RIGHT * 9.0f/8.0f, 1.0f));

        this.maze = maze;

        cellWidth = 1.0f / maze.getWidth();
        cellHeight = 1.0f / maze.getHeight();

        walls = getWalls();
    }

    /**
     * Get the value of the cell in the maze associated with certain screen coordinates.
     *
     * @param x x-axis mouse position
     * @param y y-axis mouse position
     * @return
     */
    public int getCell(double x, double y) {
        return maze.getMaze()[(int) floor((x / WINDOW_WIDTH) / cellWidth)][(int) floor((y / WINDOW_HEIGHT) / cellHeight)];
    }

    public Vector3f getCellPosition(int x, int y) {
        float xPos = ((x * cellWidth + (x + 1) * cellWidth) / 2.0f) * RIGHT * 2.0f - RIGHT;
        float yPos = ((y * cellHeight + (y + 1) * cellHeight) / 2.0f) * RIGHT * 2.0f * -9.0f / 16.0f - RIGHT * -9.0f / 16.0f;

        //System.out.println(xPos + " - " + x);

        return new Vector3f(xPos, yPos, 0.0f);
    }

    public void render() {
        terrain.draw();
        for (Wall w : walls) {
            w.render();
        }
    }

    private ArrayList<Wall> getWalls() {
        ArrayList<Wall> walls = new ArrayList<>();
        for (int i = 0; i < maze.getMaze().length; i++) {
            for (int j = 0; j < maze.getMaze()[i].length; j++) {
                if (maze.getMaze()[i][j] == 1) {
                    walls.add(new Wall(getCellPosition(j, i), new Vector3f(cellWidth * RIGHT * 2.0f, cellHeight * RIGHT * 2.0f * -9.0f / 16.0f, 4.0f)));
                }
            }
        }

        return walls;
    }

}
