package killinglewis.Shadows;

import killinglewis.Models.*;
import killinglewis.math.Matrix4f;
import killinglewis.utils.Shader;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static killinglewis.utils.Shader.*;


public class ShadowMapEntityRenderer {

    private Matrix4f projectionViewMatrix;
    private Shader shader;

    /**
     * @param shader
     *            - the simple shader program being used for the shadow render
     *            pass.
     * @param projectionViewMatrix
     *            - the orthographic projection matrix multiplied by the light's
     *            "view" matrix.
     */
    protected ShadowMapEntityRenderer(Shader shader, Matrix4f projectionViewMatrix) {
        this.shader = SHADOW_SHADER;
        this.projectionViewMatrix = projectionViewMatrix;
    }

    /**
     * Renders enitites to the shadow map. Each model is first bound and then all
     * of the entities using that model are rendered to the shadow map.
     *
     * @param level
     *            - the entities to be rendered to the shadow map.
     */
    protected void render(Level level) {
        Lewis lewis = level.getLewis();
        Terrain terrain = level.getTerrain();
        ArrayList<Wall> walls = terrain.getWalls();
        ArrayList<VertexArray> entities  = new ArrayList<>();
        for (Wall wall : walls){
            entities.add(wall.getVertexArray());
        }
        for (VertexArray model : entities) {
            bindModel(model);
            prepareInstance(model);
            glDisableVertexAttribArray(0);
            glBindVertexArray(0);
            }
        terrain.render();

    }

    /**
     * Binds a raw model before rendering. Only the attribute 0 is enabled here
     * because that is where the positions are stored in the VAO, and only the
     * positions are required in the vertex shader.
     *
     * @param model
     *            - the model to be bound.
     */
    private void bindModel(VertexArray model) {
        glBindVertexArray(model.getVao());
        glEnableVertexAttribArray(0);
    }

    /**
     * Prepares a model to be rendered. The model matrix is created in the
     * usual way and then multiplied with the projection and view matrix (often
     * in the past we've done this in the vertex shader) to create the
     * mvp-matrix. This is then loaded to the vertex shader as a uniform.
     *
     * @param model
     *            - the entity to be prepared for rendering.
     */
    private void prepareInstance(VertexArray model) {
        Matrix4f modelMatrix = Matrix4f.getTransformationMatrix(model.getScale(), model.getRotation(), model.getTranslation());
        Matrix4f mvpMatrix = projectionViewMatrix.multiply(modelMatrix);
        shader.loadModelViewProjectionMatrix(mvpMatrix);
    }

}