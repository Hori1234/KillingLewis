package killinglewis.utils;

import killinglewis.Models.Particle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static killinglewis.utils.Shader.PARTICLE_SHADER;

public class ParticleManager {

    private static List<Particle> particles = new ArrayList<>();
    private static ParticleMaker particleMaker;

    public static void intitialize() {
        particleMaker = new ParticleMaker("textures/nr_1.png");
    }

    public static void update() {
        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle p = iterator.next();
            boolean alive = p.update();
            if (!alive) {
                iterator.remove();
            }
        }
    }

    public static void renderParticles() {
        particleMaker.render(particles);
    }

    public static void addParticle(Particle particle) {
        particles.add(particle);
    }

}
