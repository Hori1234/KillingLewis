package killinglewis.utils;

import killinglewis.math.Matrix4f;

import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

    public static Shader LEWIS_SHADER;

    private int id;

    public Shader(String vertexPath, String fragmentPath) {
        id = ShaderLoader.load(vertexPath, fragmentPath);
        enable();
        //Shader.LEWIS_SHADER.setUniformMat4f("projection_matrix", Matrix4f.perspective(30, 16.0f/9.0f, 1.0f, -10.0f).getMatrix());
        setUniformMat4f("projection_matrix", Matrix4f.getOrthographicMatrix(10.0f, -10.0f, 10.0f * 16.0f/9.0f, 10.0f * 16.0f/9.0f, -15.0f, 15.0f).getMatrix());
    }

    public static void loadShaders() {
        LEWIS_SHADER = new Shader("shaders/lewis.vert", "shaders/lewis.frag");
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

    public void setUniformMat4f(String name, float[] matrix) {
        if (matrix.length != 16) {
            System.err.println("Wrong matrix size! Matrices should be 4x4.");
            return;
        }

        int uniform = glGetUniformLocation(id, name);
        glUniformMatrix4fv(uniform, false, matrix);
    }
}
