package killinglewis.utils;

import killinglewis.Entities.Camera;
import killinglewis.Entities.Light;
import killinglewis.KillingLewis;
import killinglewis.math.Matrix4f;
import killinglewis.math.Vector3f;

import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

    public static Shader LEWIS_SHADER;
    public static Shader TERRAIN_SHADER;
    public static Shader WALL_SHADER;
    public static Shader OVERLAY_SHADER;
    public static Shader CANVAS_SHADER;

    private int id;
    private static Light light = new Light(new Vector3f(10,100,10), new Vector3f(1,1,1));

    public Camera camera = Camera.getInstance();

    public Shader(String vertexPath, String fragmentPath) {
        id = ShaderLoader.load(vertexPath, fragmentPath);
        enable();
        Matrix4f projectionMatrix = Matrix4f.getOrthographicMatrix(KillingLewis.RIGHT, -1.0f * KillingLewis.RIGHT,
                KillingLewis.RIGHT * 9.0f / 16.0f, -1.0f * KillingLewis.RIGHT * 9.0f / 16.0f, KillingLewis.FAR, -1.0f * KillingLewis.FAR);
//Matrix4f projectionMatrix = Matrix4f.perspective(30, 16.0f/9.0f, 1.0f, -10.0f);
        this.setUniformMat4f("projection_matrix", projectionMatrix);
        //Adding light uniforms to shaders
        this.loadLight(light);
        this.loadViewMatrix(camera);

        disable();
    }

    public static void loadShaders() {
        LEWIS_SHADER = new Shader("shaders/lewis.vert", "shaders/lewis.frag");
        TERRAIN_SHADER = new Shader("shaders/terrain.vert", "shaders/terrain.frag");
        WALL_SHADER = new Shader("shaders/wall.vert", "shaders/wall.frag");
        OVERLAY_SHADER = new Shader("shaders/overlay.vert", "shaders/overlay.frag");
        CANVAS_SHADER = new Shader("shaders/canvas.vert", "shaders/canvas.frag");
    }

    public int getID() {
        return id;
    }

    public void enable() {
        glUseProgram(id);
    }

    public void disable() {
        glUseProgram(0);
    }

    public void loadLight(Light light){
        setUniform3f("lightPosition", light.getPosition());
        setUniform3f("lightColor", light.getColor());
    }
    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Matrix4f.getViewMatrix(camera);
        setUniformMat4f("viewMatrix",viewMatrix);
    }
    public void setUniform1i(String name, int value) {
        int uniform = glGetUniformLocation(id, name);
        glUniform1i(uniform, value);
    }

    public void setUniform1f(String name, float value) {
        int uniform = glGetUniformLocation(id, name);
        glUniform1f(uniform, value);
    }

    public void setUniform3f(String name, Vector3f value) {
        int uniform = glGetUniformLocation(id, name);
        glUniform3f(uniform, value.getX(), value.getY(), value.getZ());
    }

    public void setUniformMat4f(String name, Matrix4f matrix) {

        int uniform = glGetUniformLocation(id, name);
        glUniformMatrix4fv(uniform, false, matrix.getMatrix());
    }
}
