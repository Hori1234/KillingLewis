package killinglewis.Models;

import killinglewis.math.Vector3f;
import killinglewis.utils.Shader;
import org.lwjgl.system.CallbackI;

public class Overlay {
    VertexArray overlay;
    VertexArray overlayText = null;
    float progress;
    boolean available = true;
    float overlayTextSpacing = 0f;
    int position;

    public Overlay(String texturePath, int position, String textPath) {
        overlay = new VertexArray("res/terrain.obj", texturePath, Shader.OVERLAY_SHADER);
        if (textPath != null) {
            overlayText = new VertexArray("res/terrain.obj", textPath, Shader.OVERLAY_TXT_SHADER);
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

    public void setAvailable() {
        this.available = true;
    }

    public void setUnavailable(int timeout) {
        this.available = false;
        if (timeout > 0) {
            new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        setAvailable();
                    }
                }, timeout
            );
        }
    }

    public void setUnavailable() {
        this.setUnavailable(-1);
    }

    public void changeAvailability() {
        this.available = !this.available;
    }

    public boolean isAvailable() {
        return this.available;
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
            Shader.OVERLAY_TXT_SHADER.enable();
            float available_f = (available) ? 1f : 0f;
            Shader.OVERLAY_TXT_SHADER.setUniform1f("available", available_f);
            overlayText.translate(new Vector3f(0.68f - position * 1.5f, 0.94f, -0.5f));
            overlayText.draw();
        }



    }
}
