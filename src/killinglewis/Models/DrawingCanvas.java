package killinglewis.Models;

import killinglewis.math.Vector3f;
import killinglewis.utils.Shader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static killinglewis.KillingLewis.WINDOW_HEIGHT;
import static killinglewis.KillingLewis.WINDOW_WIDTH;

public class DrawingCanvas {
    private VertexArray canvas;
    private HashMap<Vector3f, VertexArray> squares;
    private int[][] drawing;
    private boolean isDrawing;

    public DrawingCanvas() {
        canvas = new VertexArray("res/terrain.obj", "textures/canvas.jpg", Shader.CANVAS_SHADER);
        squares = new HashMap<>();
        canvas.scale(new Vector3f(2.0f, 2.0f, 1.0f));
        canvas.translate(new Vector3f(0.0f, 0.0f, -0.6f));
        drawing = new int[28][28];

        isDrawing = false;
    }

    public void render() {
        canvas.resetScaling();
        canvas.resetTranslation();
        canvas.scale(new Vector3f(2.0f, 2.0f, 1.0f));
        canvas.translate(new Vector3f(0.0f, 0.0f, -0.6f));
        Shader.CANVAS_SHADER.enable();
        Shader.CANVAS_SHADER.setUniform3f("square_color", new Vector3f(1.0f, 1.0f, 1.0f));
        Shader.CANVAS_SHADER.setUniform1f("square_transparency", 0.3f);
        canvas.draw();

        Shader.CANVAS_SHADER.enable();
        Shader.CANVAS_SHADER.setUniform3f("square_color", new Vector3f(1.0f, 0.0f, 0.0f));
        Shader.CANVAS_SHADER.setUniform1f("square_transparency", 1.0f);
        for (Map.Entry<Vector3f, VertexArray> square : squares.entrySet()) {
            square.getValue().resetScaling();
            square.getValue().resetTranslation();
            square.getValue().scale(new Vector3f(1.0f / 14.0f * 9.0f / 16.0f, 1.0f / 14.0f, 1.0f));
            square.getValue().translate(new Vector3f((square.getKey().getX() / WINDOW_WIDTH) * 2.0f - 1.0f, (square.getKey().getY() / WINDOW_HEIGHT) * -2.0f + 1.0f, -0.65f));
            square.getValue().draw();
        }
    }

    public void drawSquare(float x, float y) {
        if (!isDrawing) {
            isDrawing = true;
        }
        VertexArray square = new VertexArray("res/terrain.obj", "textures/canvas.jpg", Shader.CANVAS_SHADER);
        squares.put(new Vector3f(x, y, -0.6f), square);
        drawing[(int) ((x / WINDOW_WIDTH) * 28)][(int) ((y / WINDOW_HEIGHT) * 28)] = 255;
    }

    public void resetDrawing() {
        squares.clear();
    }

    public void saveDrawing() {
        BufferedImage image = new BufferedImage(28, 28, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 28; j++) {
                image.setRGB(i, j, (new Color(drawing[i][j], drawing[i][j], drawing[i][j])).getRGB());
                drawing[i][j] = 0;
            }
        }

        File output = new File("output.jpg");
        try {
            ImageIO.write(image, "jpg", output);
        } catch (IOException e) {
            e.printStackTrace();
        }

        resetDrawing();

        isDrawing = false;
    }

    public boolean getIsDrawing() {
        return isDrawing;
    }
}
