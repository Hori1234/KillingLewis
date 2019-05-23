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
    /* Normal buffer object. */
    private int nbo;
    /* Texture buffer object. */
    private int tbo;
    /* Indeces buffer object. */
    private int ibo;
    /* Number of triangles. */
    private int count;
    /* Texture of this object. */
    private Texture texture;
    /* Scale, rotation and translation of this model. */
    private Vector3f scale, rotation, translation;
    /* Shader for this model. */
    private Shader shader;
    /* Width of model. */
    private float width;
    /* Height of model. */
    private float height;
    /* Depth of model. */
    private float depth;

    public VertexArray(String modelPath, String texturePath, Shader shader) {

        scale = new Vector3f(1.0f, 1.0f, 1.0f);
        rotation = new Vector3f(0.0f, 0.0f, 0.0f);
        translation = new Vector3f(0.0f, 0.0f, 0.0f);
        this.shader = shader;
        shader.enable();

        shader.setUniformMat4f("transformation", Matrix4f.identityMatrix());

        texture = new Texture(texturePath);
        glActiveTexture(GL_TEXTURE0);
        shader.setUniform1i("tex", 0);

        ModelLoader ml = new ModelLoader();
        ml.loadModel(modelPath);

        // set sizes of model
        width = getSize(ml.getVertices(), 0);
        height = getSize(ml.getVertices(), 1);
        depth = getSize(ml.getVertices(), 2);

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

        FloatBuffer normalBuffer = null;
        if(ml.getNormals() != null) {
             normalBuffer = BufferUtils.createFloatBuffer(ml.getNormals().length);
            normalBuffer.put(ml.getNormals());
            normalBuffer.flip();
        }

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
        glEnableVertexAttribArray(0);

        tbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, textureCoordsBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);

        if(normalBuffer != null) {
            nbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, nbo);
            glBufferData(GL_ARRAY_BUFFER, normalBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
        }

        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(0);
    }

    public void draw() {
        shader.enable();
        texture.bind();
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);


        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        texture.unbind();
        shader.disable();
    }

    public void translate(Vector3f translation) {
        shader.enable();
        this.translation = this.translation.add(translation);
        shader.setUniformMat4f("transformation", Matrix4f.getTransformationMatrix(this.scale, this.rotation, this.translation));
        shader.disable();
    }

    public void scale(Vector3f scale) {
        shader.enable();
        this.scale = this.scale.dot(scale);
        shader.setUniformMat4f("transformation", Matrix4f.getTransformationMatrix(this.scale, this.rotation, this.translation));
        shader.disable();
    }

    public void rotateX(float angle) {
        shader.enable();
        this.rotation = this.rotation.add(new Vector3f(angle, 0.0f, 0.0f));
        shader.setUniformMat4f("transformation", Matrix4f.getTransformationMatrix(this.scale, this.rotation, this.translation));
        shader.disable();
    }

    public void rotateY(float angle) {
        shader.enable();
        this.rotation = this.rotation.add(new Vector3f(0.0f, angle, 0.0f));
        shader.setUniformMat4f("transformation", Matrix4f.getTransformationMatrix(this.scale, this.rotation, this.translation));
        shader.disable();
    }

    public void rotateZ(float angle) {
        shader.enable();
        this.rotation = this.rotation.add(new Vector3f(0.0f, 0.0f, angle));
        this.rotation = new Vector3f(rotation.getX() % 360.0f, rotation.getY() % 360.0f, rotation.getZ() % 360.0f);
        shader.setUniformMat4f("transformation", Matrix4f.getTransformationMatrix(this.scale, this.rotation, this.translation));
        shader.disable();
    }

    public void resetTranslation() {
        translation = new Vector3f(0.0f, 0.0f, 0.0f);
    }

    public void resetRotation() {
        rotation = new Vector3f(0.0f, 0.0f, 0.0f);
    }

    public void resetRotationZ() {
        rotation = new Vector3f(rotation.getX(), rotation.getY(), 0.0f);
    }

    public void resetScaling() {
        scale = new Vector3f(1.0f, 1.0f, 1.0f);
    }

    public Vector3f getTranslation() {
        return translation;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    private float getSize(float[] array, int start) {
        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;

        for (int i = start; i < array.length; i += 3) {
            if (array[i] >= max)
                max = array[i];
            if (array[i] <= min)
                min = array[i];
        }

        return Math.abs(max - min);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getDepth() {
        return depth;
    }
}
