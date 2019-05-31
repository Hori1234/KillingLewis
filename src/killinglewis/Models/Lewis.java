package killinglewis.Models;

import killinglewis.math.Matrix4f;
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
    private float turnAngle;
    private VertexArray torso, upperLArm, lowerLArm, upperRArm, lowerRArm, upperLLeg, lowerLLeg, upperRLeg, lowerRLeg;
    float l = 0.0f;
    private final float SPEED = 0.02f;
    private final float SCALE = 0.05f;
    boolean p = false;

    public Lewis(int mazeX, int mazeY) {
        lewis = new VertexArray("res/lewis.obj", "textures/lewis.png", Shader.LEWIS_SHADER);
        lewis.scale(new Vector3f(SCALE, SCALE, SCALE));
        lewis.rotateX(90);
        this.mazeX = mazeX;
        this.mazeY = mazeY;
        isRunning = false;

        currentPos = lewis.getTranslation();
        targetPos = lewis.getTranslation();

        torso = new VertexArray("res/lewis_torso.obj", "textures/lewis.png", Shader.LEWIS_SHADER);
        upperLArm = new VertexArray("res/lewis_upper_la.obj", "textures/lewis.png", Shader.LEWIS_SHADER);
        lowerLArm = new VertexArray("res/lewis_lower_la.obj", "textures/lewis.png", Shader.LEWIS_SHADER);
        upperRArm = new VertexArray("res/lewis_upper_ra.obj", "textures/lewis.png", Shader.LEWIS_SHADER);
        lowerRArm = new VertexArray("res/lewis_lower_ra.obj", "textures/lewis.png", Shader.LEWIS_SHADER);
        upperLLeg = new VertexArray("res/lewis_upper_ll.obj", "textures/lewis.png", Shader.LEWIS_SHADER);
        lowerLLeg = new VertexArray("res/lewis_lower_ll.obj", "textures/lewis.png", Shader.LEWIS_SHADER);
        upperRLeg = new VertexArray("res/lewis_upper_rl.obj", "textures/lewis.png", Shader.LEWIS_SHADER);
        lowerRLeg = new VertexArray("res/lewis_lower_rl.obj", "textures/lewis.png", Shader.LEWIS_SHADER);
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
        float moveX = abs(deltaPos.getX()) >= SPEED ? SPEED * signum(deltaPos.getX()) : deltaPos.getX();
        float moveY = abs(deltaPos.getY()) >= SPEED ? SPEED * signum(deltaPos.getY()) : deltaPos.getY();
        float moveZ = abs(deltaPos.getZ()) >= SPEED ? SPEED * signum(deltaPos.getZ()) : deltaPos.getZ();

        Vector3f moveVector = new Vector3f(moveX, moveY, moveZ);

        float angle = (float) (toDegrees(acos(-deltaPos.getY() / deltaPos.getLength())));

        if (!Float.isNaN(angle)) {
            if (deltaPos.getX() < 0 || deltaPos.getY() > 0) {
                angle *= -1;
            }
            if (turnAngle < angle) {
                turnAngle += 10.0f;
                lewis.rotateZ(- 10.0f);
            } else if (turnAngle > angle) {
                turnAngle -= 10.0f;
                lewis.rotateZ(10.0f);
            }
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

        if (l >= 60) {
            p = true;
        } else if (l <= -60) {
            p = false;
        }

        if (p) {
            l -= SPEED * 250;
        } else {
            l += SPEED * 250;
        }

        lewis.rotateY(0);
        lewis.translate(new Vector3f(0.0f, 0.0f, -0.2f));
        torso.draw();
        rotateUpper(upperLArm, l);
        upperLArm.draw();
        rotateLower(upperLArm, lowerLArm, l, -1.0f * abs(l / 60.0f * 120));
        lowerLArm.draw();
        rotateUpper(upperRArm, -l);
        upperRArm.draw();
        rotateLower(upperLArm, lowerLArm, -l, -1.0f * abs(-l / 60.0f * 120));
        lowerRArm.draw();
        rotateUpper(upperLLeg, -l);
        upperLLeg.draw();
        rotateLower(upperLLeg, lowerLLeg, -l, abs(-l / 60.0f * 120));
        lowerLLeg.draw();
        rotateUpper(upperRLeg, l);
        upperRLeg.draw();
        rotateLower(upperRLeg, lowerRLeg, l, abs(l / 60.0f * 120));
        lowerRLeg.draw();
        lewis.translate(new Vector3f(0.0f, 0.0f, 0.2f));
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

    private void rotateUpper(VertexArray upper, float angle) {
        Matrix4f m = Matrix4f.identityMatrix();
        m.translate(upper.jointVertex.scale(-1.0f));
        m.rotateX(angle);
        m.translate(upper.jointVertex);
        m.rotateX(90);
        m.scale(lewis.getScale());
        m.rotateZ(lewis.getRotation().getZ());
        m.translate(lewis.getTranslation());
        Shader.LEWIS_SHADER.enable();
        Shader.LEWIS_SHADER.setUniformMat4f("transformation", m);
    }

    private void rotateLower(VertexArray upper, VertexArray lower, float angleUpper, float angleLower) {
        Matrix4f m = Matrix4f.identityMatrix();
        m.translate(lower.jointVertex.scale(-1.0f));
        m.rotateX(angleLower);
        m.translate(lower.jointVertex);
        m.translate(upper.jointVertex.scale(-1.0f));
        m.rotateX(angleUpper);
        m.translate(upper.jointVertex);
        m.rotateX(90);
        m.scale(lewis.getScale());
        m.rotateZ(lewis.getRotation().getZ());
        m.translate(lewis.getTranslation());
        Shader.LEWIS_SHADER.enable();
        Shader.LEWIS_SHADER.setUniformMat4f("transformation", m);
    }
}
