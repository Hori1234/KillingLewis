package killinglewis.Models;

import killinglewis.math.Vector3f;
import killinglewis.utils.Shader;

public class Terrain {
    private VertexArray terrain;

    public Terrain() {
        terrain = new VertexArray("res/terrain.obj", "textures/bricks.jpg", Shader.TERRAIN_SHADER);
        terrain.translate(new Vector3f(0.0f, 0.0f, -10.0f));
        terrain.scale(new Vector3f(1280.0f, 720.0f, 1.0f));
    }

    public void render() {
        terrain.draw();
    }

}
