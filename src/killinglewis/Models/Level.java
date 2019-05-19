package killinglewis.Models;

import killinglewis.math.Vector3f;

public class Level {
    Lewis lewis;
    Terrain terrain;

    public Level() {
        lewis = new Lewis(0, 0);
        terrain = new Terrain();

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
}
