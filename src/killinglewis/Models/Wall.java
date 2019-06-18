package killinglewis.Models;

import killinglewis.math.Vector3f;
import killinglewis.utils.Shader;

public class Wall {
    private VertexArray wall;
    private Vector3f position;
    private Vector3f size;

    public Wall(Vector3f position, Vector3f size) {
        wall = new VertexArray("res/wall.obj", "textures/wall.jpg", Shader.WALL_SHADER);
        this.position = position;
        this.size = size;

    }

    public void render() {
        wall.resetTranslation();
        wall.resetScaling();
        wall.scale(size);
        wall.translate(position);
        wall.translate(new Vector3f(0.0f, 0.0f, 0.5f));
        wall.draw();
    }

    public VertexArray getVertexArray(){
        return wall;
    }
}
