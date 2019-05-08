package killinglewis;

import killinglewis.Models.VertexArray;
import killinglewis.input.CursorPosition;
import killinglewis.input.KeyboardInput;
import killinglewis.input.MouseInput;
import killinglewis.math.Matrix4f;
import killinglewis.math.Vector3f;
import killinglewis.utils.ShaderLoader;
import killinglewis.utils.Texture;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

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
    private final int WINDOW_WIDTH = 1280;
    /* Window height. */
    private final int WINDOW_HEIGHT = 720;
    /* Current window. */
    private long window;
    /* Shader the game is using. */
    private int shader;

    private VertexArray lewis;

    int viewMatrixUniform;

    float moveHorizontal = 0.0f;
    float moveVertical = 0.0f;
    float moveZ = 0.0f;
    float rotated = 0.0f;
    float rotatedX = 0.0f;

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

        shader = ShaderLoader.load("shaders/vertexShader.vert", "shaders/fragmentShader.frag");
        glUseProgram(shader);

        float[] vertices = {
                // front
                -1.0f, -1.0f,  0.5f,
                1.0f, -1.0f,  0.5f,
                1.0f,  1.0f,  0.5f,
                -1.0f,  1.0f,  0.5f,
                // back
                -1.0f, -1.0f, -0.5f,
                1.0f, -1.0f, -0.5f,
                1.0f,  1.0f, -0.5f,
                -1.0f,  1.0f, -0.5f
        };

        byte[] indices = {
                // front
                0, 1, 2,
                2, 3, 0,
                // right
                1, 5, 6,
                6, 2, 1,
                // back
                7, 6, 5,
                5, 4, 7,
                // left
                4, 0, 3,
                3, 7, 4,
                // bottom
                4, 5, 1,
                1, 0, 4,
                // top
                3, 2, 6,
                6, 7, 3
        };


        float[] textureCoords = {
                0, 0,
                0, 1,
                1, 1,
                1, 0,
                0, 0,
                0, 1,
                1, 1,
                1, 0
        };




        Texture lewisTexture = new Texture("textures/bricks.jpg");

        int uniform = glGetUniformLocation(shader, "tex");
        glActiveTexture(GL_TEXTURE0);
        glUniform1i(uniform, 0);

        viewMatrixUniform = glGetUniformLocation(shader, "view_matrix");
        Matrix4f viewMatrix = Matrix4f.identityMatrix();
        glUniformMatrix4fv(viewMatrixUniform, false, viewMatrix.getMatrix());

        /* TO DO!! IMPLEMENT PROJECTION MATRIX. */
        int projectionMatrixUniform = glGetUniformLocation(shader, "projection_matrix");
        Matrix4f projectionMatrix = Matrix4f.getOrthographicMatrix(10.0f, -10.0f, 10.0f * 9.0f / 16.0f, -10.0f * 9.0f / 16.0f, 5.0f, -5.0f);
        glUniformMatrix4fv(projectionMatrixUniform, false, projectionMatrix.getMatrix());

        lewis = new VertexArray(vertices, textureCoords, indices, lewisTexture);
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
            moveHorizontal += 0.05f;
            Matrix4f viewMatrix = Matrix4f.translate(new Vector3f(moveHorizontal, moveVertical, moveZ)).multiply(Matrix4f.rotateY(rotated)).multiply(Matrix4f.rotateX(rotatedX));
            glUniformMatrix4fv(viewMatrixUniform, false, viewMatrix.getMatrix());
        }

        if (KeyboardInput.keys[GLFW_KEY_A]) {
            moveHorizontal -= 0.05f;
            Matrix4f viewMatrix = Matrix4f.translate(new Vector3f(moveHorizontal, moveVertical, moveZ)).multiply(Matrix4f.rotateY(rotated)).multiply(Matrix4f.rotateX(rotatedX));
            glUniformMatrix4fv(viewMatrixUniform, false, viewMatrix.getMatrix());
        }

        if (KeyboardInput.keys[GLFW_KEY_W]) {
            moveVertical += 0.05f;
            Matrix4f viewMatrix = Matrix4f.translate(new Vector3f(moveHorizontal, moveVertical, moveZ)).multiply(Matrix4f.rotateY(rotated)).multiply(Matrix4f.rotateX(rotatedX));
            glUniformMatrix4fv(viewMatrixUniform, false, viewMatrix.getMatrix());
        }

        if (KeyboardInput.keys[GLFW_KEY_S]) {
            moveVertical -= 0.05f;
            Matrix4f viewMatrix = Matrix4f.translate(new Vector3f(moveHorizontal, moveVertical, moveZ)).multiply(Matrix4f.rotateY(rotated)).multiply(Matrix4f.rotateX(rotatedX));
            glUniformMatrix4fv(viewMatrixUniform, false, viewMatrix.getMatrix());
        }

        if (KeyboardInput.keys[GLFW_KEY_S]) {
            moveVertical -= 0.05f;
            Matrix4f viewMatrix = Matrix4f.translate(new Vector3f(moveHorizontal, moveVertical, moveZ)).multiply(Matrix4f.rotateY(rotated)).multiply(Matrix4f.rotateX(rotatedX));
            glUniformMatrix4fv(viewMatrixUniform, false, viewMatrix.getMatrix());
        }

        if (KeyboardInput.keys[GLFW_KEY_T]) {
            moveZ -= 0.05f;
            Matrix4f viewMatrix = Matrix4f.translate(new Vector3f(moveHorizontal, moveVertical, moveZ)).multiply(Matrix4f.rotateY(rotated)).multiply(Matrix4f.rotateX(rotatedX));
            glUniformMatrix4fv(viewMatrixUniform, false, viewMatrix.getMatrix());
        }

        if (KeyboardInput.keys[GLFW_KEY_LEFT]) {
            rotated -= 0.05f;
            Matrix4f viewMatrix = Matrix4f.translate(new Vector3f(moveHorizontal, moveVertical, moveZ)).multiply(Matrix4f.rotateY(rotated)).multiply(Matrix4f.rotateX(rotatedX));
            glUniformMatrix4fv(viewMatrixUniform, false, viewMatrix.getMatrix());
        }

        if (KeyboardInput.keys[GLFW_KEY_RIGHT]) {
            rotated += 0.05f;
            Matrix4f viewMatrix = Matrix4f.translate(new Vector3f(moveHorizontal, moveVertical, moveZ)).multiply(Matrix4f.rotateY(rotated)).multiply(Matrix4f.rotateX(rotatedX));
            glUniformMatrix4fv(viewMatrixUniform, false, viewMatrix.getMatrix());
        }

        if (KeyboardInput.keys[GLFW_KEY_UP]) {
            rotatedX += 0.05f;
            Matrix4f viewMatrix = Matrix4f.translate(new Vector3f(moveHorizontal, moveVertical, moveZ)).multiply(Matrix4f.rotateY(rotated)).multiply(Matrix4f.rotateX(rotatedX));
            glUniformMatrix4fv(viewMatrixUniform, false, viewMatrix.getMatrix());
            System.out.println(rotatedX);
        }

        if (KeyboardInput.keys[GLFW_KEY_DOWN]) {
            rotatedX -= 0.05f;
            Matrix4f viewMatrix = Matrix4f.translate(new Vector3f(moveHorizontal, moveVertical, moveZ)).multiply(Matrix4f.rotateY(rotated)).multiply(Matrix4f.rotateX(rotatedX));
            glUniformMatrix4fv(viewMatrixUniform, false, viewMatrix.getMatrix());
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
        lewis.draw();
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
