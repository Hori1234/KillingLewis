package killinglewis.Models;

import killinglewis.math.Vector3f;
import killinglewis.utils.ParticleManager;
import killinglewis.utils.Shader;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Particle {

    public static final float GRAVITY = -50;

    VertexArray particle;
    String texturePath;
    private Vector3f position;
    private Vector3f velocity;
    Shader particleShader;

    private float gravityEffect;
    private float life;
    private float rotation;
    private float size;
    private float timeElapsed = 0.0f;
    private float startTime = (float)System.currentTimeMillis()/1000;


    public Particle(String texturePath, Shader shader, Vector3f position, Vector3f velocity, float gravityEffect, float life, float rotation, float size) {
        this.particle  = new VertexArray("res/terrain.obj",texturePath,shader);
        this.texturePath = texturePath;
        this.particleShader = shader;
        this.position = position;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.life = life;
        this.rotation = rotation;
        this.size = size;
        ParticleManager.addParticle(this);
    }
    public Particle(Vector3f position, Vector3f velocity, float gravityEffect, float life, float rotation, float size) {
        this.position = position;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.life = life;
        this.rotation = rotation;
        this.size = size;
        ParticleManager.addParticle(this);
    }


    public Vector3f getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public float getSize() {
        return size;
    }

    public boolean update() {
        float y_velocity = GRAVITY * gravityEffect * (float)System.currentTimeMillis()/1000;
        Vector3f change = velocity.add(new Vector3f(1,1,1));
        change = change.scale((float) System.currentTimeMillis()/1000);
        position = position.add(change);
        timeElapsed  = (float)System.currentTimeMillis()/1000 - startTime;
        return timeElapsed < life;
    }

}
