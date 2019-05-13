package killinglewis.Models;

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
    Texture texture;

    public VertexArray(float[] vertices, float[] textureCoords, int[] indices, Texture texture) {
        this.texture = texture;

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices);
        vertexBuffer.flip(); // flip the buffer for opengl

        FloatBuffer textureCoordsBuffer = BufferUtils.createFloatBuffer(textureCoords.length);
        textureCoordsBuffer.put(textureCoords);
        textureCoordsBuffer.flip();

        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);
        indexBuffer.put(indices);
        indexBuffer.flip();

        count = indices.length; // set the number of triangles

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

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(0);
    }

    public void draw() {
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
    }
}
