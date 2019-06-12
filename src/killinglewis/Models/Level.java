package killinglewis.Models;

import killinglewis.ArtificialIntelligence.AStar;
import killinglewis.ModelLoader.NNLoader;
import killinglewis.Spells.Flame;
import killinglewis.Spells.Soak;
import killinglewis.Spells.Spell;
import killinglewis.input.CursorPosition;
import killinglewis.math.Vector3f;
import killinglewis.utils.InteractionManager;
import killinglewis.utils.Maze;
import killinglewis.utils.Shader;

import java.util.ArrayList;

public class Level {
    private Lewis lewis;
    private Terrain terrain;
    private Maze maze;
    private DrawingCanvas canvas;
    private ArrayList<Integer> path;
    private Overlay health, mana, instruct_short;
    private progressOverlay healthProgress, manaProgress;
    private boolean canvasActive;

    public boolean placeObstacleClick = false;

    public InteractionManager interact;

    public Level(Maze maze) {
        this.maze = maze;
        lewis = new Lewis(maze.getStartX(), maze.getStartY());
        terrain = new Terrain(maze);
        canvas = new DrawingCanvas();
        interact = new InteractionManager();
        canvasActive = false;

        interact.addSpell(new Flame(0.2f, 0.2f, 0.02f));
        interact.addSpell(new Soak(0.2f, 0.2f));

        lewis.moveTo(terrain.getCellPosition(lewis.getMazeX(), lewis.getMazeY()));

        this.createOverlays();
    }

    public void createOverlays() {
        // health overlay
        healthProgress = new progressOverlay("textures/health.png", 0.83f, 0.85f, Shader.OVERLAY_SHADER);
        healthProgress.scale(0.2f, 0.05f, 1f);
        health = new Overlay("textures/lewis_health_txt.png", 0.85f, 0.91f,  Shader.OVERLAY_TXT_SHADER);
        health.scale(0.25f, 0.07f, 1f);

        // mana overlay
        manaProgress = new progressOverlay("textures/mana.png", -0.85f, 0.85f, Shader.OVERLAY_SHADER);
        manaProgress.scale(0.2f, 0.05f, 1f);
        mana = new progressOverlay("textures/player_mana_txt.png", -0.85f, 0.91f, Shader.OVERLAY_TXT_SHADER);
        mana.scale(0.22f, 0.07f, 1f);

        // instructions overlay
        instruct_short = new Overlay("textures/instructions_short.png", 0.85f, -0.85f, Shader.OVERLAY_TXT_SHADER);
        instruct_short.scale(0.2f, 0.2f, 1f);
    }

    public void render() {
        terrain.render();
        lewis.render();
        healthProgress.render();
        health.render();
        manaProgress.render();
        mana.render();
        instruct_short.render();

        if (canvasActive) {
            canvas.render();
        }

        update();
    }

    public void update() {
        healthProgress.setProgress(interact.getHealth());   // Set the progress bar to the current health
        manaProgress.setProgress(interact.getMana());       //
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

    public void checkResult(){

        System.out.print("Spell " + NNLoader.resultedLabel + " " + "casted");

        if (NNLoader.resultedLabel == 2 && interact.getMana() - interact.getSpell("Triangle").getManaCost() >= 0) {
            this.castSpell("Triangle");
        }

        else if (NNLoader.resultedLabel == 1 && interact.getMana() - interact.getSpell("Circle").getManaCost() >= 0) {
            this.castSpell("Circle");
        }

        else if (NNLoader.resultedLabel == 3) {
            placeObstacleClick = true;
        }
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
