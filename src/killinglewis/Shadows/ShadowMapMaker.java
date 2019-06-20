package killinglewis.Shadows;

import killinglewis.Entities.Camera;
import killinglewis.Entities.Light;
import killinglewis.KillingLewis;
import killinglewis.Models.Level;
import killinglewis.Models.VertexArray;
import killinglewis.math.Matrix4f;
import killinglewis.math.Vector2f;
import killinglewis.math.Vector3f;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Map;

import static killinglewis.math.Matrix4f.identityMatrix;
import static org.lwjgl.opengl.GL11.*;
import static killinglewis.utils.Shader.*;

public class ShadowMapMaker {

    private static final int SHADOW_MAP_SIZE = 1024;
    /** Frame Buffer that will contain the depth values**/
    private DepthFrameBuffer shadowFbo;
    /**Shadow box that pertains info about the view cuboid**/
    private ShadowBox shadowBox;
    /**Orthogonal projection matrix**/
    private Matrix4f projectionMatrix = new Matrix4f();
    /**Light's view matrix**/
    private Matrix4f lightViewMatrix = new Matrix4f();
    /**Projection view matrix which is a product of both the view**/
    private Matrix4f projectionViewMatrix = new Matrix4f();
    /**Offset matrix needed for shadow map space conversion**/
    private Matrix4f offset = createOffset();

    private ShadowMapEntityRenderer entityRenderer;

    /**
     * Creates instances of the important objects needed for rendering the scene
     * to the shadow map.
     * @param camera the camera.
     */
    public ShadowMapMaker(Camera camera) {
        shadowBox = new ShadowBox(lightViewMatrix, camera);
        shadowFbo = new DepthFrameBuffer(SHADOW_MAP_SIZE, SHADOW_MAP_SIZE);
        entityRenderer = new ShadowMapEntityRenderer(SHADOW_SHADER, projectionViewMatrix);
    }

    public void render(Level level, Light sun) {
        shadowBox.update();
        Vector3f sunPosition = sun.getPosition();
        Vector3f lightDirection = new Vector3f(-sunPosition.getX(), -sunPosition.getY(), -sunPosition.getZ());
        prepare(lightDirection, shadowBox);
        entityRenderer.render(level);
        finish();
    }

    /**
     * Converts a world space position into a 2D coordinate on the shadow map.
     * @return To-shadow-map-space matrix.
     */
    public Matrix4f getToShadowMapSpaceMatrix() {
        return offset.multiply(projectionViewMatrix);
    }

    /**
     * Clean upFBO on closing.
     */
    public void cleanUp() {
        shadowFbo.delete();
    }

    /**
     * @return The ID of the shadow map texture
     */
    public int getShadowMap() {
        return shadowFbo.getShadow_map();
    }

    /**
     * @return The light's "view" matrix.
     */
    protected Matrix4f getLightSpaceTransform() {
        return lightViewMatrix;
    }

    /**
     * Prepare for the shadow render pass. Also binds the
     * shadows FBO so that everything rendered after this gets rendered to the
     * FBO. It also enables depth testing, and clears any data that is in the
     * FBOs depth attachment from last frame. The simple shader program is also
     * started.
     *
     * @param lightDirection the direction of the light rays coming from the sun.
     * @param box the shadow box
     */
    private void prepare(Vector3f lightDirection, ShadowBox box) {
        updateOrthoProjectionMatrix();
        updateLightViewMatrix(lightDirection);
        projectionViewMatrix =  projectionMatrix.multiply(lightViewMatrix);
        shadowFbo.bindFB();
        SHADOW_SHADER.enable();
    }

    /**
     * Finish the shadow render pass. Stops the shader and unbinds the shadow
     * FBO, so everything rendered after this point is rendered to the screen,
     * rather than to the shadow FBO.
     */
    private void finish() {
        SHADOW_SHADER.disable();
        shadowFbo.unbindFB();
    }

    /**
     * Updates the light's viewing matrix. This creates a view matrix which
     * will line up the direction of the "view cuboid" with the direction of the
     * light. The light itself has no position, so the "view" matrix is centered
     * at the center of the "view cuboid". The created view matrix determines
     * where and how the "view cuboid" is positioned in the world. The size of
     * the view cuboid, however, is determined by the projection matrix.
     *
     * @param direction
     *            - the light direction, and therefore the direction that the
     *            "view cuboid" should be pointing.
     * @param center
     *            - the center of the "view cuboid" in world space.
     */
    private void updateLightViewMatrix(Vector3f direction) {
        direction.normalise();
        Vector3f center = new Vector3f(0,0,0);
        lightViewMatrix =  identityMatrix();
        float pitch = (float) Math.acos(new Vector2f(direction.getX(), direction.getZ()).length());
        lightViewMatrix.rotateX(pitch);
        float yaw = (float) Math.toDegrees(((float) Math.atan(direction.getX() / direction.getZ())));
        yaw = direction.getZ() > 0 ? yaw - 180 : yaw;
        lightViewMatrix.rotateY(-yaw);
        lightViewMatrix.translate(center);
    }

    /**
     * Creates the orthographic projection matrix. This projection matrix
     * basically sets the width, length and height of the "view cuboid"
     *
     * @param width shadow box width.
     * @param height shadow box height.
     * @param length shadow box length.
     */
    private void updateOrthoProjectionMatrix() {
        projectionMatrix = Matrix4f.getOrthographicMatrix(1.0f*KillingLewis.RIGHT, LEFT,
                TOP, BOTTOM, 1.0f * KillingLewis.FAR, NEAR);
    }

    /**
     * Create the offset for part of the conversion to shadow map space.
     *
     * @return The offset as a matrix
     */
    private static Matrix4f createOffset() {
        Matrix4f offset = new Matrix4f();
        offset.translate(new Vector3f(0.5f, 0.5f, 0.5f));
        offset.scale(new Vector3f(0.5f, 0.5f, 0.5f));
        return offset;
    }
}
