package killinglewis.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class CursorPosition extends GLFWCursorPosCallback {

    /* X coordinate position of mouse. */
    public static double xpos;
    /* Y coordinate position of mouse. */
    public static double ypos;

    @Override
    public void invoke(long window, double xpos, double ypos) {
        this.xpos = xpos;
        this.ypos = ypos;
    }
}
