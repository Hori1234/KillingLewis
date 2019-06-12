package killinglewis.Models;

import killinglewis.math.Vector3f;
import killinglewis.utils.Shader;

public class progressOverlay extends Overlay {
    float progress;
    boolean available = true;

    public progressOverlay(String overlayPath, float pos_x, float pos_y, Shader shader) {
        super(overlayPath, pos_x, pos_y, shader);
        this.progress = 1f;
    }

    public progressOverlay(String overlayPath, float pos_x, float pos_y) {
        super(overlayPath, pos_x, pos_y, Shader.OVERLAY_SHADER);
    }

    @Override
    public void render() {
        if (visible) {
            this.overlay.resetTranslation();
            this.overlay.translate(new Vector3f(pos_x, pos_y, -0.5f));
            this.overlayShader.enable();
            this.overlayShader.setUniform1f("progress", progress);
            this.overlay.draw();
        }
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }
}
