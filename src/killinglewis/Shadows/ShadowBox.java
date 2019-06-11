package killinglewis.Shadows;

import killinglewis.Entities.Camera;
import killinglewis.KillingLewis;
import killinglewis.math.Matrix4f;
import killinglewis.math.Vector3f;
import killinglewis.math.Vector4f;

import static killinglewis.KillingLewis.*;
import static killinglewis.utils.Shader.NEAR;


public class ShadowBox {
    private static final float OFFSET = 10;
    private static final Vector4f UP = new Vector4f(0, 1, 0, 0);
    private static final Vector4f FORWARD = new Vector4f(0, 0, -1, 0);
    private static final float SHADOW_DISTANCE = 100;

    private float minX, maxX;
    private float minY, maxY;
    private float minZ, maxZ;
    private Matrix4f lightViewMatrix;
    private Camera camera;

    private float farHeight, farWidth, nearHeight, nearWidth;

    /**
     * Creates a new shadow box which is the area where shadows will be rendered.
     * @param lightViewMatrix
     * @param camera
     */
    protected ShadowBox(Matrix4f lightViewMatrix, Camera camera) {
        this.lightViewMatrix = lightViewMatrix;
        this.camera = camera;
        calculateWidthsAndHeights(60);
    }

    /**
     * Updates the bounds of the shadow box based on the camera's view frustum and light direction
     */
    protected void update() {
        Matrix4f rotation = calculateCameraRotationMatrix();
        Vector3f forwardVector = new Vector3f(rotation.multiply(FORWARD));
        Vector3f toFar = forwardVector;
        toFar.scale(SHADOW_DISTANCE);
        Vector3f toNear = forwardVector;
        toNear.scale(NEAR);
        Vector3f centerNear = toNear.add(camera.getPosition());
        Vector3f centerFar = toFar.add(camera.getPosition());

        Vector4f[] points = calculateFrustumVertices(rotation, forwardVector, centerNear,
                centerFar);

        boolean first = true;
        for (Vector4f point : points) {
            if (first) {
                minX = point.getX();
                maxX = point.getX();
                minY = point.getY();
                maxY = point.getY();
                minZ = point.getZ();
                maxZ = point.getZ();
                first = false;
                continue;
            }
            if (point.getX() > maxX) {
                maxX = point.getX();
            } else if (point.getX() < minX) {
                minX = point.getX();
            }
            if (point.getY() > maxY) {
                maxY = point.getY();
            } else if (point.getY() < minY) {
                minY = point.getY();
            }
            if (point.getZ() > maxZ) {
                maxZ = point.getZ();
            } else if (point.getZ() < minZ) {
                minZ = point.getZ();
            }
        }
        maxZ += OFFSET;

    }

    /**
     * Computer the center of the cuboid for viewing in light space then
     * converts it to world space using the inverse light's view matrix.
     *
     * @return The center of the view cuboid in world space.
     */
    protected Vector3f getCenter() {
        float x = (minX + maxX) / 2f;
        float y = (minY + maxY) / 2f;
        float z = (minZ + maxZ) / 2f;
        Vector4f cen = new Vector4f(x, y, z, 1);
        Matrix4f invertedLight = Matrix4f.invert(lightViewMatrix);
        Vector4f multi = invertedLight.multiply(cen);
        return new Vector3f(multi);
    }

    /**
     * @return The width of the orthographic projection area
     */
    protected float getWidth() {
        return maxX - minX;
    }

    /**
     * @return The height of the orthographic projection area
     */
    protected float getHeight() {
        return maxY - minY;
    }

    /**
     * @return The length of the orthographic projection area
     */
    protected float getLength() {
        return maxZ - minZ;
    }

    /**
     * Calculates the vertices positions for all corners of the view frustum
     * in light coordinates.
     *
     * @param rotation camera's rotation.
     * @param forwardVector the direction that the camera is aiming, and thus the
     *                      direction of the frustum.
     * @param centerNear    the center point of the frustum's near plane.
     * @param centerFar     the center point of the frustum's  far
     *                      plane.
     * @return The positions of the vertices of the frustum in light space.
     */
    private Vector4f[] calculateFrustumVertices(Matrix4f rotation, Vector3f forwardVector,
                                                Vector3f centerNear, Vector3f centerFar) {
        Vector3f upVector = new Vector3f(rotation.multiply(UP));
        Vector3f rightVector = forwardVector.cross(upVector);
        Vector3f downVector = new Vector3f(-upVector.getX(), -upVector.getY(), -upVector.getZ());
        Vector3f leftVector = new Vector3f(-rightVector.getX(), -rightVector.getY(), -rightVector.getZ());
        Vector3f farTop = centerFar.add(new Vector3f(upVector.getX() * farHeight,
                upVector.getY() * farHeight, upVector.getZ() * farHeight));
        Vector3f farBottom = centerFar.add(new Vector3f(downVector.getX() * farHeight,
                downVector.getY() * farHeight, downVector.getZ() * farHeight));
        Vector3f nearTop = centerNear.add(new Vector3f(upVector.getX() * nearHeight,
                upVector.getY() * nearHeight, upVector.getZ() * nearHeight));
        Vector3f nearBottom = centerNear.add(new Vector3f(downVector.getX() * nearHeight,
                downVector.getY() * nearHeight, downVector.getZ() * nearHeight));
        Vector4f[] points = new Vector4f[8];
        points[0] = calculateLightSpaceFrustumCorner(farTop, rightVector, farWidth);
        points[1] = calculateLightSpaceFrustumCorner(farTop, leftVector, farWidth);
        points[2] = calculateLightSpaceFrustumCorner(farBottom, rightVector, farWidth);
        points[3] = calculateLightSpaceFrustumCorner(farBottom, leftVector, farWidth);
        points[4] = calculateLightSpaceFrustumCorner(nearTop, rightVector, nearWidth);
        points[5] = calculateLightSpaceFrustumCorner(nearTop, leftVector, nearWidth);
        points[6] = calculateLightSpaceFrustumCorner(nearBottom, rightVector, nearWidth);
        points[7] = calculateLightSpaceFrustumCorner(nearBottom, leftVector, nearWidth);
        return points;
    }

    /**
     * Calculates one of the corner vertices of the view frustum in world space
     * and converts it to light space.
     *
     * @param startPoint  the starting center point on the view frustum.
     * @param direction   the direction of the corner from the start point.
     * @param width       the distance of the corner from the start point.
     * @return - The relevant corner vertex of the view frustum in light space.
     */
    private Vector4f calculateLightSpaceFrustumCorner(Vector3f startPoint, Vector3f direction,
                                                      float width) {
        Vector3f point = startPoint.add(
                new Vector3f(direction.getX() * width, direction.getY() * width, direction.getZ() * width));
        Vector4f point4f = new Vector4f(point.getX(), point.getY(), point.getZ(), 1f);
        point4f = lightViewMatrix.multiply(point4f);
        return point4f;
    }

    /**
     * @return Camera's rotation matrix
     */
    private Matrix4f calculateCameraRotationMatrix() {
        Matrix4f rotation = new Matrix4f();
        rotation.rotateY((float) Math.toRadians(-camera.getYaw()));
        rotation.rotateX((float) Math.toRadians(-camera.getPitch()));
        return rotation;
    }

    /**
     * Calculates the width and height of the near and far planes of the
     * camera's view frustum.
     * @param FOV camera's field of view
     */
    private void calculateWidthsAndHeights(float FOV) {
        farWidth = (float) (SHADOW_DISTANCE * Math.tan(Math.toRadians(FOV)));
        nearWidth = (float) (NEAR
                * Math.tan(Math.toRadians(FOV)));
        farHeight = farWidth / ASPECT_RATIO;
        nearHeight = nearWidth / ASPECT_RATIO;
    }

}


