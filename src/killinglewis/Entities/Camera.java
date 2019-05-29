package killinglewis.Entities;

import killinglewis.input.KeyboardInput;
import killinglewis.math.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {

    /**Camera's Position**/
    private Vector3f position = new Vector3f(0,0,0);
    /**Camera's yaw**/
    private float yaw = -2 ;
    /**Camera's pitch**/
    private float pitch = -2;
    /**Camera's Roll**/
    private float roll = 1;
    /**Camera Instance Variable**/
    private static Camera instance = null;

    private Camera(){}

    /**
     * Ensures that only one instance of camera can exist at a time and returns that instance.
     */
    public static  Camera getInstance(){
        if(instance == null){
            instance = new Camera();
        }
        return instance;
    }

    /**
     *Method for moving Camera with keyboard inputs.
     */
    public void moveCamera() {

        if (KeyboardInput.keys[GLFW_KEY_W]) {
            this.pitch += 1f;
        }
        if (KeyboardInput.keys[GLFW_KEY_A]) {
            this.yaw -= 1f;
        }
        if (KeyboardInput.keys[GLFW_KEY_S]) {
            this.pitch -= 1f;
        }
        if (KeyboardInput.keys[GLFW_KEY_D]) {
            this.yaw += 1f;
        }
        if (KeyboardInput.keys[GLFW_KEY_Z]) {
            this.roll -= 1f;
        }
        if (KeyboardInput.keys[GLFW_KEY_X]) {
            this.roll += 1f;
        }
    }


    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
