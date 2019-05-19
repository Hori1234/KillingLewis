package killinglewis.Models;

import killinglewis.math.Vector3f;
import killinglewis.utils.Shader;

import static java.lang.Math.floor;
import static killinglewis.KillingLewis.WINDOW_WIDTH;
import static killinglewis.KillingLewis.WINDOW_HEIGHT;

public class Terrain {
    private VertexArray terrain;
    private int[][] maze;
    float cellWidth;
    float cellHeight;

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

    public void render() {
        terrain.draw();
    }

}
