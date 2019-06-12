package killinglewis.Models;

import killinglewis.math.Vector3f;
import killinglewis.utils.Shader;

public class Overlay {
    VertexArray overlay;
    String overlayPath;
    float pos_x;
    float pos_y;
    Shader overlayShader;
    boolean visible = true;
    boolean available = true;

    public Overlay(String overlayPath, float pos_x, float pos_y, Shader shader) {
        this.overlay = new VertexArray("res/terrain.obj", overlayPath, shader);
        this.overlayPath = overlayPath;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.overlayShader = shader;
    }

    public Overlay(String overlayPath, Shader shader) {
        this(overlayPath, 0f, 0f, shader);
    }

    public Overlay(String overlayPath, float pos_x, float pos_y) {
        this(overlayPath, pos_x, pos_y, Shader.OVERLAY_TXT_SHADER);
    }

    public Overlay(String overlayPath) {
        this(overlayPath, 0f, 0f, Shader.OVERLAY_TXT_SHADER);
    }

    public void setShader(Shader shader) {
        this.overlay = new VertexArray("res/terrain.obj", this.overlayPath, shader);
    }

    public void setPos(float x, float y) {
        this.pos_x = x;
        this.pos_y = y;
    }

    public void scale(float x, float y, float z) {
        this.overlay.scale(new Vector3f(x, y, z));
    }

    public void show() {
        this.visible = true;
    }

    public void hide() {
        this.visible = false;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setAvailable() {
        this.available = true;
    }

    public void setUnavailable() {
        this.setUnavailable(-1);
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

    public boolean isAvailable() {
        return this.available;
    }

    public void render() {
        if (visible) {
            this.overlay.resetTranslation();
            this.overlay.translate(new Vector3f(pos_x, pos_y, -0.5f));
            this.overlayShader.enable();
            float available_f = (available) ? 1f : 0f;
            this.overlayShader.setUniform1f("available", available_f);
            this.overlay.draw();
        }
    }
}
