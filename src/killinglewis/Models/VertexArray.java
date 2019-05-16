package killinglewis.Models;

import killinglewis.math.Matrix4f;
import killinglewis.math.Vector3f;
import killinglewis.utils.ModelLoader;
import killinglewis.utils.Shader;
import killinglewis.utils.Texture;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;

public class VertexArray {

    /* Vertex array object. */
    private int vao;
    /* Vertex buffer object. */
    private int vbo;
    /* Texture buffer object. */
    private int tbo;
    /* Indeces buffer object. */
    private int ibo;
    /* Number of triangles. */
    private int count;
    /* Texture of this object. */
    private Texture texture;
    /* Transformation matrix of this model. */
    private Matrix4f transformationMatrix;
    /* Shader for this model. */
    private Shader shader;

    public VertexArray(String modelPath, String texturePath, Shader shader) {
        transformationMatrix = Matrix4f.identityMatrix();
        this.shader = shader;
        shader.enable();

        shader.setUniformMat4f("transformation", transformationMatrix);

        texture = new Texture(texturePath);
        glActiveTexture(GL_TEXTURE0);
        shader.setUniform1i("tex", 0);

        ModelLoader ml = new ModelLoader();
        ml.loadModel(modelPath);

        bindModel(ml);
        shader.disable();
    }

    private void bindModel(ModelLoader ml) {
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(ml.getVertices().length);
        vertexBuffer.put(ml.getVertices());
        vertexBuffer.flip(); // flip the buffer for opengl

        FloatBuffer textureCoordsBuffer = BufferUtils.createFloatBuffer(ml.getTCoords().length);
        textureCoordsBuffer.put(ml.getTCoords());
        textureCoordsBuffer.flip();

        IntBuffer indexBuffer = BufferUtils.createIntBuffer(ml.getFaces().length);
        indexBuffer.put(ml.getFaces());
        indexBuffer.flip();

        count = ml.getFaces().length; // set the number of triangles

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        tbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, textureCoordsBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

        //glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(0);
    }

    public void draw() {
        shader.enable();
        texture.bind();
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);


        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        texture.unbind();
        shader.disable();
    }

    public void translate(Vector3f translation) {
        shader.enable();
        transformationMatrix = transformationMatrix.translate(translation);
        shader.setUniformMat4f("transformation", transformationMatrix);
        shader.disable();
    }

    public void scale(Vector3f scale) {
        shader.enable();
        transformationMatrix = transformationMatrix.scale(scale);
        shader.setUniformMat4f("transformation", transformationMatrix);
        shader.disable();
    }

    public void rotateX(float angle) {
        shader.enable();
        transformationMatrix = transformationMatrix.rotateX(angle);
        shader.setUniformMat4f("transformation", transformationMatrix);
        shader.disable();
    }

    public void rotateY(float angle) {
        shader.enable();
        transformationMatrix = transformationMatrix.rotateY(angle);
        shader.setUniformMat4f("transformation", transformationMatrix);
        shader.disable();
    }

    public void rotateZ(float angle) {
        shader.enable();
        transformationMatrix = transformationMatrix.rotateZ(angle);
        shader.setUniformMat4f("transformation", transformationMatrix);
        shader.disable();
    }
}
