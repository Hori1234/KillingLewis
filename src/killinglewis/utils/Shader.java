package killinglewis.utils;

import killinglewis.math.Matrix4f;

import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

    public static Shader LEWIS_SHADER;
    public static Shader TERRAIN_SHADER;

    private int id;

    public Shader(String vertexPath, String fragmentPath) {
        id = ShaderLoader.load(vertexPath, fragmentPath);
        enable();
        Matrix4f projectionMatrix = Matrix4f.getOrthographicMatrix(10.0f, -10.0f, 10.0f * 9.0f / 16.0f, -10.0f * 9.0f / 16.0f, -15.0f, 15.0f);
        //Matrix4f projectionMatrix = Matrix4f.perspective(30, 16.0f/9.0f, 1.0f, -10.0f);
        this.setUniformMat4f("projection_matrix", projectionMatrix);

        disable();
    }

    public static void loadShaders() {
        LEWIS_SHADER = new Shader("shaders/lewis.vert", "shaders/lewis.frag");
        TERRAIN_SHADER = new Shader("shaders/terrain.vert", "shaders/terrain.frag");
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

    public void setUniform1i(String name, int value) {
        int uniform = glGetUniformLocation(id, name);
        glUniform1i(uniform, value);
    }

    public void setUniform1f(String name, float value) {
        int uniform = glGetUniformLocation(id, name);
        glUniform1f(uniform, value);
    }

    public void setUniformMat4f(String name, Matrix4f matrix) {

        int uniform = glGetUniformLocation(id, name);
        glUniformMatrix4fv(uniform, false, matrix.getMatrix());
    }
}
