package killinglewis;

import killinglewis.Entities.Camera;
import killinglewis.ModelLoader.NNLoader;
import killinglewis.Models.Level;
import killinglewis.Models.Terrain;
import killinglewis.Shadows.ShadowMapMaker;
import killinglewis.input.CursorPosition;
import killinglewis.input.KeyboardInput;
import killinglewis.input.MouseInput;
import killinglewis.input.SpellInput;
import killinglewis.utils.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.security.Key;

import static killinglewis.utils.Shader.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;


/**
 *
 * @author popacosmin
 */
public class KillingLewis implements Runnable {

    /* Thread handling the Neural Network. */
    private Thread nnThread;
    private String path = "C:\\Users\\swhyo\\Desktop\\PythonCNN\\NeuralNetTest.py";
    private String inPath = "NeuralNetwork\\NeuralNetTest.py";
    private boolean isNnRunning = false;
    /* Neural Network Loader. */
    private NNLoader loader;
    /* Thread handling the rendering. */
    private Thread graphicsThread;
    /* Window width.*/
    public static final int WINDOW_WIDTH = 1280;
    /* Window height. */
    public static final int WINDOW_HEIGHT = 720;
    /*Aspect Ratio. */
    public static final float ASPECT_RATIO = (float) WINDOW_WIDTH / (float) WINDOW_HEIGHT;
    public static final float RIGHT = 10.0f, FAR = -15.0f;
    /* Current window. */
    private long window;
    /* Shader the game is using. */
    private int shader;
    /* Target FPS. */
    private final int TARGET_FPS  = 60;
    /* Level currently displayed. */
     public Level level;
    /* Last key pressed */
    private int lastKey = GLFW_KEY_TAB;
    /* SpellInput object*/
    SpellInput rawPosition = new SpellInput();
    public Camera camera = Camera.getInstance();
    ShadowMapMaker shadowMaker;

    private boolean oKeyDown = false;

    private static boolean NEW_WINDOW_ON_RESTART = false;

    /**
     * Initialize GLFW window.
     */
    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        //Initialize Thread of the NN and the NN loader
        loader = new NNLoader(inPath,true);
        loader.start();

        // initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // set OpenGl version hints for window
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        // create the window if it does not exist yet
        if (window == NULL || NEW_WINDOW_ON_RESTART) {
            window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "Killing Lewis", NULL, NULL);
        }

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

        level = new Level(new Maze("mazes/BigMaze.txt"));
        level.findPath();
        shadowMaker = new ShadowMapMaker(camera);
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

        if (isNnRunning) {

            if (!NNLoader.processing) {
                System.out.println("pizda ma-tii friptule");
                level.checkResult();
                isNnRunning = false;
            }
        }


        if (KeyboardInput.keys[GLFW_KEY_SPACE]) {
            if (!level.getLewis().getIsRunning()) {
                level.runToNextCell();
            }
            lastKey = GLFW_KEY_SPACE;
        }

        if (KeyboardInput.keys[GLFW_KEY_TAB]) {
            if (lastKey != GLFW_KEY_TAB) {
                rawPosition.clear();
            }
            rawPosition.addMovement(CursorPosition.xpos, CursorPosition.ypos);
            level.setCanvasActive(true);

            lastKey = GLFW_KEY_TAB;
        } else {
            level.setCanvasActive(false);
        }

        if (KeyboardInput.keys[GLFW_KEY_O]) {
            level.placeObstruction(CursorPosition.xpos, CursorPosition.ypos);
        }

        if (KeyboardInput.keys[GLFW_KEY_I]) {
            level.openExpOverlay();
        }

        if (KeyboardInput.keys[GLFW_KEY_ESCAPE] && level.expOverlayActive()) {
            level.closeExpOverlay();
        }

        if (KeyboardInput.keys[GLFW_KEY_ENTER] && level.finished()) {
            this.run();
        }

        if (MouseInput.mouseButton[GLFW_MOUSE_BUTTON_LEFT]) {
            if (level.getCanvasActive()) {
                level.drawOnCanvas((float) CursorPosition.xpos, (float) CursorPosition.ypos);
            }

            if(level.placeObstacleClick) {
                level.placeObstruction(CursorPosition.xpos, CursorPosition.ypos);
                level.placeObstacleClick = false;
            }

        } else {
            if (level.getCanvasActive() && level.getCanvas().getIsDrawing()) {
                level.getCanvas().saveDrawing();
                isNnRunning = true;
                NNLoader.processing= true;
            }
        }

    }

    private void render() {
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        level.render();
        //shadowMaker.render(level,light);
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
            updateCamera();
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

        loader.interrupt();
    }
    private void updateCamera(){
        camera.moveCamera();
        LEWIS_SHADER.enable();
        LEWIS_SHADER.loadViewMatrix(camera);
        LEWIS_SHADER.disable();
        WALL_SHADER.enable();
        WALL_SHADER.loadViewMatrix(camera);
        WALL_SHADER.disable();
        TERRAIN_SHADER.enable();;
        TERRAIN_SHADER.loadViewMatrix(camera);
        TERRAIN_SHADER.disable();
    }



    /**
     *
     * @return shadow map texture id
     */
    private int getShadowMapTexture() {
        return shadowMaker.getShadowMap();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        (new KillingLewis()).start();
    }



}
