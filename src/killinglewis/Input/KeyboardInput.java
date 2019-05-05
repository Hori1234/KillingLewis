package killinglewis.Input;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

public class KeyboardInput extends GLFWKeyCallback {

    /* Static array of booleans representing each pressable key. */
    public static boolean[] keys = new boolean[400];

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        // if the key has not been released, set its corresponding boolean value to true; otherwise set to false
        keys[key] = (action != GLFW_RELEASE);
    }
}
