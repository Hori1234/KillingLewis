package killinglewis.Models;

import killinglewis.math.Vector3f;
import killinglewis.utils.Shader;

public class Overlay {
    VertexArray overlay;
    float progress;
    int position;

    public Overlay(String texturePath, int position) {
        overlay = new VertexArray("res/terrain.obj", texturePath, Shader.OVERLAY_SHADER);
        progress = 1.0f;
        this.position = position;

        overlay.scale(new Vector3f(1.0f / 3.0f, 1.0f / 16.0f, 1.0f));
    }

    public void changeProgress(float change) {
        if (progress + change >= 0.0f || progress + change <= 1.0f) {
            progress += change;
        }
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public void render() {
        overlay.resetTranslation();
        overlay.translate(new Vector3f(0.75f - position * 1.5f, 0.96f, -0.5f));
        Shader.OVERLAY_SHADER.enable();
        Shader.OVERLAY_SHADER.setUniform1f("progress", progress);
        overlay.draw();
    }
}
