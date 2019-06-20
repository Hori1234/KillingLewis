package killinglewis.utils;


import killinglewis.Models.Particle;
import killinglewis.Models.VertexArray;
import killinglewis.math.Vector3f;

import java.util.List;

import static killinglewis.utils.Shader.PARTICLE_SHADER;
import static org.lwjgl.opengl.GL11.*;


public class ParticleMaker {

    protected VertexArray quad;
    protected Shader shader;
    private int  type;

    public ParticleMaker(int type) {
        this.type = type;
        if(type == 0) {
            quad = new VertexArray("res/wall.obj", "textures/fire.png", PARTICLE_SHADER);
        }else if(type == 1) {
            quad = new VertexArray("res/wall.obj", "textures/bubble.png", PARTICLE_SHADER);
        }
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

        shader.disable();
    }

    private void finish() {
        shader.enable();

    }
}
