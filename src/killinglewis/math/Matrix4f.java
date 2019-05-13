package killinglewis.math;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Matrix4f {
    private float[] matrix = new float[16];

    /**
     * Returns the identity matrix in the form of a Matrix4f.
     *
     * @return the identity matrix
     */
    public static Matrix4f identityMatrix() {
        Matrix4f identity = new Matrix4f();

        for (int i = 0; i < 4; i++) {
            identity.matrix[i + i * 4] = 1.0f;
        }

        return identity;
    }

    public float[] getMatrix() {
        return matrix;
    }

    public static Matrix4f translate(Vector3f translation) {
        Matrix4f translated = identityMatrix();

        translated.getMatrix()[12] = translation.getX();
        translated.getMatrix()[13] = translation.getY();
        translated.getMatrix()[14] = translation.getZ();

        return translated;
    }

    public static Matrix4f rotateY(float angle) {
        Matrix4f rotated = identityMatrix();

        rotated.getMatrix()[0 + 0 * 4] = (float) cos(angle);
        rotated.getMatrix()[0 + 2 * 4] = (float) (-1.0f * sin(angle));
        rotated.getMatrix()[2 + 0 * 4] = (float) sin(angle);
        rotated.getMatrix()[2 + 2 * 4] = (float) cos(angle);

        return rotated;
    }

    public static Matrix4f rotateX(float angle) {
        Matrix4f rotated = identityMatrix();

        rotated.getMatrix()[1 + 1 * 4] = (float) cos(angle);
        rotated.getMatrix()[1 + 2 * 4] = (float) (-1.0f * sin(angle));
        rotated.getMatrix()[2 + 1 * 4] = (float) sin(angle);
        rotated.getMatrix()[2 + 2 * 4] = (float) cos(angle);

        return rotated;
    }

    public Matrix4f multiply(Matrix4f other) {
        Matrix4f multiplied = new Matrix4f();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                float sum = 0.0f;
                for (int k = 0; k < 4; k++) {
                    sum += this.getMatrix()[i + k * 4] * other.getMatrix()[j + k * 4];
                }
                multiplied.getMatrix()[i + j * 4] = sum;
            }
        }

        return multiplied;
    }

    /**
     * Returns the orthographic matrix in the form of a Matrix4f.
     *
     * @param right screen coordinate
     * @param left screen coordinate
     * @param top screen coordinate
     * @param bottom screen coordinate
     * @param far far plane
     * @param near near plane
     * @return the orthographic projection matrix
     */
    public static Matrix4f getOrthographicMatrix(float right, float left, float top, float bottom, float far, float near) {
        Matrix4f orthographic = identityMatrix();

        orthographic.matrix[0 + 0 * 4] = 2.0f / (right - left);
        orthographic.matrix[1 + 1 * 4] = 2.0f / (top - bottom);
        orthographic.matrix[2 + 2 * 4] = 2.0f / (far - near);

        orthographic.matrix[12] = - (right + left) / (right - left);
        orthographic.matrix[13] = - (top + bottom) / (top - bottom);
        orthographic.matrix[14] = - (far + near) / (far - near);

        return orthographic;
    }

    public static Matrix4f getPerspectivePrjMatrix(float right, float left, float top, float bottom, float far, float near) {
        Matrix4f projection = identityMatrix();

        projection.matrix[0 + 0 * 4] = (2.0f * near) / (right - left);
        projection.matrix[1 + 1 * 4] = (2.0f * near) / (top - bottom);
        projection.matrix[2 + 2 * 4] = - 1.0f * (far + near) / (far - near);
        projection.matrix[3 + 3 * 4] = 0.0f;

        projection.matrix[0 + 2 * 4] = (right + left) / right - left;
        projection.matrix[1 + 2 * 4] = (top + bottom) / top - bottom;
        projection.matrix[3 + 2 * 4] = -1.0f;

        projection.matrix[2 + 3 * 4] = -2.0f * far * near / (far - near);

        return projection;
    }
}
