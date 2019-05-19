package killinglewis;

import killinglewis.Models.Terrain;
import killinglewis.Models.VertexArray;
import killinglewis.input.CursorPosition;
import killinglewis.input.KeyboardInput;
import killinglewis.input.MouseInput;
import killinglewis.math.Matrix4f;
import killinglewis.math.Vector3f;
import killinglewis.utils.ModelLoader;
import killinglewis.utils.Shader;
import killinglewis.utils.ShaderLoader;
import killinglewis.utils.Texture;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static killinglewis.utils.Shader.loadShaders;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;


/**
 *
 * @author popacosmin
 */
public class KillingLewis implements Runnable {

    /* Thread handling the rendering. */
    private Thread graphicsThread;
    /* Window width.*/
    public static final int WINDOW_WIDTH = 1280;
    /* Window height. */
    public static final int WINDOW_HEIGHT = 720;
    /* Current window. */
    private long window;
    /* Shader the game is using. */
    private int shader;
    /* Target FPS. */
    private final int TARGET_FPS  = 60;

    private VertexArray lewis;

    Terrain terrain;

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
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); // enable alpha blending

        System.out.println("OpenGL: " + glGetString(GL_VERSION));
        glClearColor(0.0f, 0.7f, 0.6f, 0.0f);

        loadShaders();

        lewis = new VertexArray("res/cow.obj", "textures/cow.jpg", Shader.LEWIS_SHADER);
        lewis.rotateX(90);
        lewis.scale(new Vector3f(0.2f, 0.2f, 0.2f));
        terrain = new Terrain();
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

        if (KeyboardInput.keys[GLFW_KEY_D]) {
            lewis.translate(new Vector3f(0.05f, 0.0f, 0.0f));
        }

        if (KeyboardInput.keys[GLFW_KEY_A]) {
            lewis.translate(new Vector3f(-0.05f, 0.0f, 0.0f));
        }

        if (KeyboardInput.keys[GLFW_KEY_W]) {
            lewis.translate(new Vector3f(0.0f, 0.05f, 0.0f));
        }

        if (KeyboardInput.keys[GLFW_KEY_S]) {
            lewis.translate(new Vector3f(0.0f, -0.05f, 0.0f));
        }

        if (KeyboardInput.keys[GLFW_KEY_T]) {
            lewis.translate(new Vector3f(0.0f, 0.0f, -0.05f));
        }

        if (KeyboardInput.keys[GLFW_KEY_R]) {
            lewis.translate(new Vector3f(0.00f, 0.0f, 0.05f));
        }

        if (KeyboardInput.keys[GLFW_KEY_LEFT]) {
            lewis.rotateY(-0.5f);
        }

        if (KeyboardInput.keys[GLFW_KEY_RIGHT]) {
            lewis.rotateY(0.5f);
        }

        if (KeyboardInput.keys[GLFW_KEY_UP]) {
            lewis.rotateX(0.5f);
        }

        if (KeyboardInput.keys[GLFW_KEY_DOWN]) {
            lewis.rotateX(-0.5f);
        }

        if (MouseInput.mouseButton[GLFW_MOUSE_BUTTON_LEFT]) {
            System.out.println(terrain.getCell(CursorPosition.xpos, CursorPosition.ypos));
        }


    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        lewis.draw();
        terrain.render();
        glfwSwapBuffers(window);
    }

    /**
     * The main game loop.
     */
    private void loop() {
        double lastTime = glfwGetTime();
        double secondTime = lastTime;
        int frameCounter = 0;

        while (!glfwWindowShouldClose(window)) {
            // render and update the game state
            render();
            update();
            //increase the frames counter
            frameCounter++;

            while (glfwGetTime() < lastTime + 1.0/TARGET_FPS) {
                // do nothing until we can render next frame
            }

            // print the FPS if one second has passed
            if (lastTime >= secondTime + 1) {
                System.out.println(frameCounter + " FPS");
                frameCounter = 0;
                secondTime = lastTime;
            }

            lastTime = glfwGetTime();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        (new KillingLewis()).start();
    }

}
