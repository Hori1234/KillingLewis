package killinglewis.Models;

import killinglewis.math.Vector3f;
import killinglewis.utils.Shader;

import static java.lang.Math.floor;
import static killinglewis.KillingLewis.*;

public class Terrain {
    private VertexArray terrain;
    private int[][] maze;
    private float cellWidth;
    private float cellHeight;

    public Terrain() {
        terrain = new VertexArray("res/terrain.obj", "textures/bricks.jpg", Shader.TERRAIN_SHADER);
        terrain.translate(new Vector3f(0.0f, 0.0f, -10.0f));
        terrain.scale(new Vector3f(WINDOW_WIDTH, WINDOW_HEIGHT, 1.0f));

        // dummy maze
        maze = new int[2][2];
        maze[0][0] = 0;
        maze[0][1] = 1;
        maze[1][0] = 2;
        maze[1][1] = 3;

        cellWidth = 1.0f / maze.length;
        cellHeight = 1.0f / maze[0].length;

    }

    /**
     * Get the value of the cell in the maze associated with certain screen coordinates.
     *
     * @param x x-axis mouse position
     * @param y y-axis mouse position
     * @return
     */
    public int getCell(double x, double y) {
        return maze[(int) floor((x / WINDOW_WIDTH) / cellWidth)][(int) floor((y / WINDOW_HEIGHT) / cellHeight)];
    }

    public Vector3f getCellPosition(int x, int y) {
        float xPos = ((x * cellWidth + (x + 1) * cellWidth) / 2.0f) * RIGHT * 2.0f - RIGHT;
        float yPos = ((y * cellHeight + (y + 1) * cellHeight) / 2.0f) * RIGHT * 2.0f * -9.0f / 16.0f - RIGHT * -9.0f / 16.0f;

        return new Vector3f(xPos, yPos, 0.0f);
    }

    public void render() {
        terrain.draw();
    }

}
