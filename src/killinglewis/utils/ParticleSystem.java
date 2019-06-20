package killinglewis.utils;

import killinglewis.Models.Particle;
import killinglewis.math.Vector3f;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class ParticleSystem {

    private float pps;
    private float speed;
    private float gravityComplient;
    private float lifeLength;

    public ParticleSystem(float pps, float speed, float gravityComplient, float lifeLength) {
        this.pps = pps;
        this.speed = speed;
        this.gravityComplient = gravityComplient;
        this.lifeLength = lifeLength;
    }

    public void generateParticles(Vector3f systemCenter){
        float delta = (float)glfwGetTime()/1000;
        float particlesToCreate = pps * delta;
        int count = (int) Math.floor(particlesToCreate);
        float partialParticle = particlesToCreate % 1;
        for(int i=0;i<count;i++){
            emitParticle(systemCenter);
        }
        if(Math.random() < partialParticle){
            emitParticle(systemCenter);
        }
    }

    private void emitParticle(Vector3f center){
        float dirX = (float) Math.random() * 2f - 1f;
        float dirY = (float) Math.random() * 2f - 1f;
        Vector3f velocity = new Vector3f(dirX, dirY,1 );
        velocity.normalise();
        velocity.scale(speed);
        new Particle(new Vector3f(center), velocity, gravityComplient, lifeLength, 1, 0.2f);
    }



}