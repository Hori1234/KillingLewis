package killinglewis.Input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class CursorPosition extends GLFWCursorPosCallback {

    public static double xpos;
    public static double ypos;

    @Override
    public void invoke(long window, double xpos, double ypos) {
        this.xpos = xpos;
        this.ypos = ypos;
    }
}
