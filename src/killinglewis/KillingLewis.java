package killinglewis;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


/**
 *
 * @author popacosmin
 */
public class KillingLewis implements Runnable {

    /* Thread handling the rendering. */
    private Thread graphicsThread;
    /* Window width.*/
    private final int WINDOW_WIDTH = 1280;
    /* Window height. */
    private final int WINDOW_HEIGHT = 720;
    /* Current window. */
    private long window;

    /**
     * Initialize GLFW window.
     */
    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        // initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "Killing Lewis", NULL, NULL);

        if (window == NULL) {
            throw new RuntimeException("Failed to create the window.");
        }

        // Get the primary monitor
        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        // Center the window
        glfwSetWindowPos(window, (videoMode.width() - WINDOW_WIDTH) / 2, (videoMode.height() - WINDOW_HEIGHT) / 2);

        // Display the window
        glfwMakeContextCurrent(window);
        glfwShowWindow(window);
    }

    @Override
    public void run() {
        init();
        loop();
    }

    private void start() {
        graphicsThread = new Thread(this, "Rendering thread");
        graphicsThread.run();
    }

    private void update() {
        glfwPollEvents();
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glfwSwapBuffers(window);
    }

    /**
     * The main game loop.
     */
    private void loop() {
        GL.createCapabilities();
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        while (!glfwWindowShouldClose(window)) {
            render();
            update();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        (new KillingLewis()).start();
    }

}
