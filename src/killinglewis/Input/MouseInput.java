package killinglewis.Input;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseInput extends GLFWMouseButtonCallback {

    public static boolean[] mouseButton = new boolean[12];

    @Override
    public void invoke(long window, int button, int action, int mods) {
        mouseButton[button] = (action != GLFW_RELEASE);
    }
}
