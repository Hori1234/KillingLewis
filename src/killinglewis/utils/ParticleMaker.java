package killinglewis.utils;


import killinglewis.Entities.Camera;
import killinglewis.Models.Particle;
import killinglewis.Models.VertexArray;
import killinglewis.math.Matrix4f;
import killinglewis.math.Vector3f;

import java.util.List;

import static killinglewis.math.Matrix4f.identityMatrix;
import static killinglewis.utils.Shader.PARTICLE_SHADER;
import static org.lwjgl.opengl.GL11.*;


public class ParticleMaker {

    private VertexArray quad;
    private Shader shader;
    private String texturePath;

    public ParticleMaker(String texturePath) {
        this.texturePath = texturePath;
        quad = new VertexArray("res/terrain.obj", "textures/cow.jpg", PARTICLE_SHADER);
        this.shader = PARTICLE_SHADER;
    }

    protected void render(List<Particle> particles) {
        Camera camera  = Camera.getInstance();
        Matrix4f viewMatrix = Matrix4f.getViewMatrix(camera);
        //prepare();
        shader.enable();
        for (Particle particle : particles) {
            updateModelViewMatrix(particle.getPosition(),particle.getRotation(),particle.getSize(),viewMatrix);
            quad.draw();
        }
        //finish();
        shader.disable();

    }

    public void updateModelViewMatrix(Vector3f position, float rotation, float scale,Matrix4f viewMatrix) {
        Matrix4f modelmatrix = identityMatrix();
        modelmatrix.translate(position);
        modelmatrix.matrix[0 + 0 * 4] =  viewMatrix.matrix[0 + 0 * 4];
        modelmatrix.matrix[0 + 1 * 4] =  viewMatrix.matrix[1 + 0 * 4];
        modelmatrix.matrix[0 + 2 * 4] =  viewMatrix.matrix[2 + 0 * 4];
        modelmatrix.matrix[1 + 1 * 4] =  viewMatrix.matrix[1 + 1 * 4];
        modelmatrix.matrix[1 + 2 * 4] =  viewMatrix.matrix[2 + 1 * 4];
        modelmatrix.matrix[2 + 0 * 4] =  viewMatrix.matrix[0 + 2 * 4];
        modelmatrix.matrix[2 + 1 * 4] =  viewMatrix.matrix[1 + 2 * 4];
        modelmatrix.matrix[2 + 2 * 4] =  viewMatrix.matrix[2 + 2 * 4];
        modelmatrix.rotateZ(rotation);
        modelmatrix.scale(new Vector3f(scale,scale,scale));
        Matrix4f modelViewMatrix = viewMatrix.multiply(modelmatrix);
        shader.enable();
        shader.loadModelViewMatrix(modelViewMatrix);
        shader.disable();
    }
    /**
     * Enables alpha blending and stops particle from being rendered to the depth buffer.
     */
    private void prepare() {
        shader.enable();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthMask(false);
        shader.disable();
    }

    private void finish() {
        shader.enable();
        glDepthMask(true);
        glDisable(GL_BLEND);
        shader.disable();
    }
}
