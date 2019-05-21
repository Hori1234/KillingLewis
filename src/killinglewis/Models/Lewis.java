package killinglewis.Models;

import killinglewis.math.Vector3f;
import killinglewis.utils.Shader;

import static java.lang.Math.*;
import static killinglewis.KillingLewis.*;

public class Lewis {
    private VertexArray lewis;
    private int mazeX;
    private int mazeY;
    private Vector3f currentPos;
    private Vector3f targetPos;
    private boolean isRunning;

    public Lewis(int mazeX, int mazeY) {
        lewis = new VertexArray("res/lewis.obj", "textures/cow.jpg", Shader.LEWIS_SHADER);
        lewis.rotateX(90);
        lewis.scale(new Vector3f(0.2f, 0.2f, 0.2f));
        this.mazeX = mazeX;
        this.mazeY = mazeY;
        isRunning = false;

        currentPos = lewis.getTranslation();
        targetPos = lewis.getTranslation();
    }

    /**
     * Move this object to a certain location slowly (i.e. running)
     *
     * @param target the target location
     */
    public void runTo(Vector3f target) {
        targetPos = target;
        isRunning = true;
    }

    /**
     * Move this object to a certain location instantly.
     *
     * @param target the target location
     */
    public void moveTo(Vector3f target) {
        lewis.resetTranslation();
        lewis.translate(target);
        currentPos = target;
    }

    private void runToNext() {
        Vector3f deltaPos = targetPos.subtract(currentPos);
        float moveX = abs(deltaPos.getX()) >= 0.05f ? 0.05f * signum(deltaPos.getX()) : deltaPos.getX();
        float moveY = abs(deltaPos.getY()) >= 0.05f ? 0.05f * signum(deltaPos.getY()) : deltaPos.getY();
        float moveZ = abs(deltaPos.getZ()) >= 0.05f ? 0.05f * signum(deltaPos.getZ()) : deltaPos.getZ();

        Vector3f moveVector = new Vector3f(moveX, moveY, moveZ);

        float angle = (float) (toDegrees(acos(deltaPos.getX() / deltaPos.getLength())));

        System.out.println(angle);

        if (!Float.isNaN(angle)) {
            lewis.resetRotationZ();
            lewis.rotateZ(angle);
        }

        lewis.translate(moveVector);

        currentPos = lewis.getTranslation();

        if(deltaPos.getX() == 0.0f && deltaPos.getY() == 0.0f && deltaPos.getZ() == 0.0f) {
            isRunning = false;
        }
    }

    public void render() {
        if (isRunning) {
            runToNext();
        }
        lewis.draw();
    }

    public boolean getIsRunning() {
        return isRunning;
    }

    /**
     * Gets the x-axis coordinate in the maze where this object is currently placed.
     *
     * @return x-axis maze coordinate
     */
    public int getMazeX() {
        return mazeX;
    }

    /**
     * Gets the y-axis coordinate in the maze where this object is currently placed.
     *
     * @return y-axis maze coordinate
     */
    public int getMazeY() {
        return mazeY;
    }
}
