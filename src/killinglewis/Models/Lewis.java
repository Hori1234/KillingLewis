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
    float r = 0.0f;
    float l = 0.0f;
    float z = 0.0f;
    boolean p = false;

    public Lewis(int mazeX, int mazeY) {
        lewis = new VertexArray("res/lewis.obj", "textures/lewis.png", Shader.LEWIS_SHADER);
        lewis.rotateX(90);
        //lewis.scale(new Vector3f(0.2f, 0.2f, 0.2f));
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
        float moveX = abs(deltaPos.getX()) >= 0.05f ? 0.05f * signum(deltaPos.getX()) : deltaPos.getX();
        float moveY = abs(deltaPos.getY()) >= 0.05f ? 0.05f * signum(deltaPos.getY()) : deltaPos.getY();
        float moveZ = abs(deltaPos.getZ()) >= 0.05f ? 0.05f * signum(deltaPos.getZ()) : deltaPos.getZ();

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
        Vector3f c = lewis.getTranslation();
        if (isRunning) {
            runToNext();
        }

//        if (l >= 45) {
//            p = true;
//        } else if (l <= -45) {
//            p = false;
//        }
//
//        z = lewis.getRotation().getZ();
//        lewis.resetRotation();
//        lewis.rotateZ(z);
//        lewis.rotateX(90);
        torso.draw();
        upperLArm.draw();
        lowerLArm.draw();
        upperRArm.draw();
        lowerRArm.draw();
//        if (p) {
//            r += 3.0f;
//        } else {
//            r -= 3.0f;
//        }
//
//        lewis.resetRotation();
//        lewis.rotateZ(z);
//        lewis.rotateX(r + 90);
        Matrix4f  m = Matrix4f.identityMatrix();
        m.translate(upperLLeg.jointVertex.scale(-1.0f));
        m.rotateX(-45);
        m.translate(upperLLeg.jointVertex);
        m.rotateX(90);
        m.translate(c);
        //m.scale(new Vector3f(0.2f, 0.2f, 0.2f));
        Shader.LEWIS_SHADER.enable();
        Shader.LEWIS_SHADER.setUniformMat4f("transformation", m);
        upperLLeg.draw();
        m = Matrix4f.identityMatrix();
        m.translate(lowerLLeg.jointVertex.scale(-1.0f));
        m.rotateX(90);
        m.translate(lowerLLeg.jointVertex);
        m.translate(upperLLeg.jointVertex.scale(-1.0f));
        m.rotateX(-45);
        m.translate(upperLLeg.jointVertex);
        m.rotateX(90);
        m.translate(c);
        Shader.LEWIS_SHADER.enable();
        Shader.LEWIS_SHADER.setUniformMat4f("transformation", m);
        lowerLLeg.draw();
        lewis.rotateY(0);
        upperRLeg.draw();
        lowerRLeg.draw();
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
