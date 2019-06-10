package killinglewis.Models;

import killinglewis.math.Vector3f;
import killinglewis.utils.Shader;

public class Overlay {
    VertexArray overlay;
    VertexArray overlayText = null;
    float progress;
    float overlayTextSpacing = 0f;
    int position;

    public Overlay(String texturePath, int position, String textPath) {
        overlay = new VertexArray("res/terrain.obj", texturePath, Shader.OVERLAY_SHADER);
        if (textPath != null) {
            overlayText = new VertexArray("res/terrain.obj", textPath, Shader.OVERLAY_SHADER);
            overlayTextSpacing = -0.08f;
        }
        progress = 1.0f;
        this.position = position;

        overlay.scale(new Vector3f(1.0f / 3.0f, 1.0f / 16.0f, 1.0f));
        if (overlayText != null) overlayText.scale(new Vector3f(.2f, .05f, 1.0f));

    }

    public Overlay(String texturePath, int position) {
        this(texturePath, position, null);
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
        overlay.translate(new Vector3f(0.75f - position * 1.5f, 0.96f + overlayTextSpacing, -0.5f));
        Shader.OVERLAY_SHADER.enable();
        Shader.OVERLAY_SHADER.setUniform1f("progress", progress);
        overlay.draw();
        if (overlayText != null) {
            overlayText.resetTranslation();
            overlayText.translate(new Vector3f(0.68f - position * 1.5f, 0.94f, -0.5f));
            overlayText.draw();
        }



    }
}
