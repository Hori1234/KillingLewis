package killinglewis.input;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseInput extends GLFWMouseButtonCallback {

    /* Static array of booleans representing each pressable mouse button. */
    public static boolean[] mouseButton = new boolean[12];

    @Override
    public void invoke(long window, int button, int action, int mods) {
        // if the button has not been released, set its corresponding boolean value to true; otherwise set to false
        mouseButton[button] = action == GLFW_PRESS;
    }
}
