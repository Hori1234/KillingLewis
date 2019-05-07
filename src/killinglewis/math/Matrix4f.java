package killinglewis.math;

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
}
