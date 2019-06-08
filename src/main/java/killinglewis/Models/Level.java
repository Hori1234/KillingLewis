package killinglewis.Models;

import killinglewis.ArtificialIntelligence.AStar;
import killinglewis.ModelLoader.NNLoader;
import killinglewis.math.Vector3f;
import killinglewis.utils.Maze;

import java.util.ArrayList;

public class Level {
    private Lewis lewis;
    private Terrain terrain;
    private Maze maze;
    private DrawingCanvas canvas;
    private ArrayList<Integer> path;
    private Overlay health, mana;
    private boolean canvasActive;


    public Level(Maze maze) {
        this.maze = maze;
        lewis = new Lewis(maze.getStartX(), maze.getStartY());
        terrain = new Terrain(maze);
        canvas = new DrawingCanvas();
        health = new Overlay("textures/health.png", 0);
        mana = new Overlay("textures/mana.png", 1);
        canvasActive = false;

        lewis.moveTo(terrain.getCellPosition(lewis.getMazeX(), lewis.getMazeY()));
    }

    public void render() {
        terrain.render();
        lewis.render();
        health.render();
        mana.render();

        if (canvasActive) {
            canvas.render();
        }
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

    public void setCanvasActive(boolean isActive) {
        if (isActive == canvasActive) {
            return;
        }

        canvasActive = isActive;

        if (!isActive) {
            canvas.resetDrawing();
        }
    }

    public boolean getCanvasActive() {
        return canvasActive;
    }
    public void drawOnCanvas(float x, float y) {
        canvas.drawSquare(x, y);
    }

    public void checkResult(){
//        String[] result = NNLoader.result;
////        if (result != null) {
////            for (int j=0; j<result.length; j++){
////                System.out.print(result[j]);
////            }
////            System.out.println();
////            boolean notfound = true;
////            int spellNumber = 0;
////            int i = -1;
////            while (notfound && i < result.length -1 ) {
////                i++;
////                if (result[i].equals("1")) {
////                    //System.out.println(i);
////                    spellNumber = i;
////                    notfound = true;
////                }
////            }
////            System.out.println("Spell " + spellNumber + " " + "casted");
////        }
        System.out.print("Spell " + NNLoader.resultedLabel + " " + "casted");
    }
    public DrawingCanvas getCanvas() {
        return canvas;
    }
}
