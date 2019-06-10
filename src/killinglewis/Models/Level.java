package killinglewis.Models;

import killinglewis.ArtificialIntelligence.AStar;
import killinglewis.Spells.Spell;
import killinglewis.math.Vector3f;
import killinglewis.utils.InteractionManager;
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
    public InteractionManager interact;

    public Level(Maze maze) {
        this.maze = maze;
        lewis = new Lewis(maze.getStartX(), maze.getStartY());
        terrain = new Terrain(maze);
        canvas = new DrawingCanvas();
        health = new Overlay("textures/health.png", 0, "textures/lewis_health_txt.png");
        mana = new Overlay("textures/mana.png", 1, "textures/player_mana_txt.png");
        interact = new InteractionManager();
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

        update();
    }

    public void update() {
        health.setProgress(interact.getHealth());   // Set the progress bar to the current health
        mana.setProgress(interact.getMana());       //
        lewis.setSpeed(interact.getStamina() * 0.02f);  // Set Lewis' speed according to his stamina
        interact.regenerate();
    }

    public void renderShadow() {
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

    public void findPath() {
        path = AStar.getSolutionPath(maze);
    }

    public void runToNextCell() {
        Vector3f next = getNextCell();
        lewis.updatePosition((int) next.getY(), (int) next.getX());
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

    public DrawingCanvas getCanvas() {
        return canvas;
    }


    /** Cast a spell according to the figure that was drawn
     * @param figure string representation of the figure that was drawn
     */
    public void castSpell(String figure) {
        interact.getSpell(figure).cast(interact);
    }

    /**
     * @param x mouse coord
     * @param y mouse coord
     */
    public void placeObstruction(double x, double y) {
        if (interact.enoughMana(0.1f) && mana.isAvailable()) {
            interact.reduceMana(0.1f);
            terrain.placeObstruction(x, y, lewis.getMazeX(), lewis.getMazeY());
            findPath();
            mana.setUnavailable(2000);
        }
    }
    
}
