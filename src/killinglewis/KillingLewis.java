package killinglewis;

import killinglewis.input.CursorPosition;
import killinglewis.input.KeyboardInput;
import killinglewis.input.MouseInput;
import killinglewis.utils.ShaderLoader;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
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
    /* Shader the game is using. */
    int shader;

    /**
     * Initialize GLFW window.
     */
    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        // initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // set OpenGl version hints for window
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        // create the window
        window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "Killing Lewis", NULL, NULL);

        // throw exception if window creation has failed
        if (window == NULL) {
            throw new RuntimeException("Failed to create the window.");
        }

        // Set key, cursor and mouse button callbacks
        glfwSetKeyCallback(window, new KeyboardInput());
        glfwSetCursorPosCallback(window, new CursorPosition());
        glfwSetMouseButtonCallback(window, new MouseInput());

        // Get the primary monitor
        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        // Center the window
        glfwSetWindowPos(window, (videoMode.width() - WINDOW_WIDTH) / 2, (videoMode.height() - WINDOW_HEIGHT) / 2);

        // Display the window
        glfwMakeContextCurrent(window);
        glfwShowWindow(window);

        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        System.out.println("OpenGL: " + glGetString(GL_VERSION));
        glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

        shader = ShaderLoader.load("shaders/vertexShader.vert", "shaders/fragmentShader.frag");
        glUseProgram(shader);

        // initialize and bind vertex array object
        int vao = glGenVertexArrays();
        glBindVertexArray(vao);
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
        if (KeyboardInput.keys[GLFW_KEY_ESCAPE]) {
            glfwSetWindowShouldClose(window, true);
        }

        if (KeyboardInput.keys[GLFW_KEY_SPACE]) {
            System.out.println(CursorPosition.xpos);
        }

        if (MouseInput.mouseButton[GLFW_MOUSE_BUTTON_LEFT]) {
            System.out.println("Left click!");
        }
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        glfwSwapBuffers(window);
    }

    /**
     * The main game loop.
     */
    private void loop() {
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
