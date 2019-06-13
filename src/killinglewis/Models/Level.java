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
    private Overlay health, mana, instruct_short, instruct_exp, endoverlay, obstruct_avail;
    private progressOverlay healthProgress, manaProgress;
    private boolean canvasActive;
    private boolean ongoing; // the level is not finished

    public InteractionManager interact;

    public Level(Maze maze) {
        this.maze = maze;
        lewis = new Lewis(maze.getStartX(), maze.getStartY());
        terrain = new Terrain(maze);
        canvas = new DrawingCanvas();
        canvasActive = false;
        ongoing = true;
        endoverlay = null;

        interact = new InteractionManager();
        interact.addSpell(new Flame(0.3f, 0.2f, 0.1f));
        interact.addSpell(new Soak(0.2f, 0.5f));

        lewis.moveTo(terrain.getCellPosition(lewis.getMazeX(), lewis.getMazeY()));

        this.createOverlays();
    }

    public void render() {
        terrain.render();
        lewis.render();

        if (canvasActive) {
            canvas.render();
        }

        if (ongoing) {  // Render the overlays while the game is running
            health.render();
            mana.render();
            healthProgress.render();
            manaProgress.render();
            instruct_short.render();
            instruct_exp.render();
            obstruct_avail.render();
        }

        if (endoverlay != null) {
            endoverlay.render();
        }

        update();
    }

    public void update() {
        if (maze.reachedGoal(lewis.getMazeX(), lewis.getMazeY())) { // Lewis won
            endGame("lewis");
        } else if (interact.getHealth() == 0) {
            endGame("player");
        } else {
            healthProgress.setProgress(interact.getHealth());   // Set the progress bar to the current health
            manaProgress.setProgress(interact.getMana());       //
            lewis.setSpeed(interact.getStamina() * 0.01f);  // Set Lewis' speed according to his stamina
            interact.regenerate();
        }
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

    public void checkResult() {

        System.out.print("Spell " + NNLoader.resultedLabel + " " + "casted");

        if (NNLoader.resultedLabel == 1 && interact.getMana() - interact.getSpell("Flame").getManaCost() >= 0) {
            this.castSpell("Flame");
        } else if (NNLoader.resultedLabel == 2 && interact.getMana() - interact.getSpell("Soak").getManaCost() >= 0) {
            this.castSpell("Soak");
        } else if (NNLoader.resultedLabel == 3) {
            interact.addObstruct();
            obstruct_avail.setAvailable();
            System.out.println("obstructions avail: " + String.valueOf(interact.getObstruct()));
        }
    }

    public DrawingCanvas getCanvas() {
        return canvas;
    }

    public void castSpell(String name) {
        interact.getSpell(name).cast(interact);
    }

    public void placeObstruction(double x, double y) {
        if (interact.enoughMana(0.2f) && mana.isAvailable() && interact.enoughObstruct()) {
            interact.reduceMana(interact.obstructionCost());
            terrain.placeObstruction(x, y, lewis.getMazeX(), lewis.getMazeY());
            findPath();
            mana.setUnavailable(2000);
            interact.reduceObstruct();
            if (!interact.enoughObstruct()) obstruct_avail.setUnavailable();
        }
    }

    public void endGame(String winner) {
        this.ongoing = false;

        String overlayPath = "textures/end_" + winner + "_win.png";
        if (endoverlay == null) endoverlay = new Overlay(overlayPath, 0,0, Shader.OVERLAY_TXT_SHADER);
    }

    public boolean finished() {
        return !this.ongoing;
    }

    public void openExpOverlay() {
        instruct_short.hide();
        instruct_exp.show();
    }

    public void closeExpOverlay() {
        instruct_short.show();
        instruct_exp.hide();
    }

    public boolean expOverlayActive() {
        return !instruct_short.isVisible();
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
        instruct_exp = new Overlay("textures/instructions_expand.png", 0f, 0f, Shader.OVERLAY_TXT_SHADER);
        instruct_exp.scale(1f, 1f, 1f);
        instruct_exp.hide();

        // obstruction available
        obstruct_avail = new Overlay("textures/obstruct_avail.png", -0.8f, -0.9f, Shader.OVERLAY_TXT_SHADER);
        obstruct_avail.scale(0.3f, 0.1f, 1f);
        obstruct_avail.setUnavailable();
    }
}
