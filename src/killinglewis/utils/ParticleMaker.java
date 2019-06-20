package killinglewis.utils;


import killinglewis.Models.Particle;
import killinglewis.Models.VertexArray;
import killinglewis.math.Vector3f;

import java.util.List;

import static killinglewis.utils.Shader.PARTICLE_SHADER;
import static org.lwjgl.opengl.GL11.*;


public class ParticleMaker {

    private VertexArray quad;
    private Shader shader;
    private String texturePath;

    public ParticleMaker(String texturePath) {
        this.texturePath = texturePath;
        quad = new VertexArray("res/wall.obj", "textures/cow.jpg", PARTICLE_SHADER);
        this.shader = PARTICLE_SHADER;
    }

    protected void render(List<Particle> particles) {
        //prepare();
        for (Particle particle : particles) {
            quad.resetTranslation();
            quad.resetScaling();
            quad.resetRotation();
            quad.scale(new Vector3f(particle.getSize(), particle.getSize(), particle.getSize()));
            quad.rotateX(particle.getRotation());
            quad.translate(particle.getPosition());
            quad.draw();
        }

        //finish();

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
