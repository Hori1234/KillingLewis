package killinglewis.math;

public class Matrix4d {
    private double[][] matrix = new double[4][4];

    /**
     * Returns the identity matrix in the form of a Matrix4d.
     *
     * @return the identity matrix
     */
    public static Matrix4d identityMatrix() {
        Matrix4d identity = new Matrix4d();

        for (int i = 0; i < 4; i++) {
            identity.matrix[i][i] = 1.0d;
        }

        return identity;
    }

    /**
     * Returns the orthographic matrix in the form of a Matrix4d.
     *
     * @param right screen coordinate
     * @param left screen coordinate
     * @param top screen coordinate
     * @param bottom screen coordinate
     * @param far far plane
     * @param near near plane
     * @return the orthographic projection matrix
     */
    public static Matrix4d getOrthographicMatrix(double right, double left, double top, double bottom, double far, double near) {
        Matrix4d orthographic = identityMatrix();

        orthographic.matrix[0][0] = 2.0 / (right - left);
        orthographic.matrix[1][1] = 2.0 / (top - bottom);
        orthographic.matrix[2][2] = 2.0 / (far - near);

        orthographic.matrix[0][3] = - (right + left) / (right - left);
        orthographic.matrix[1][3] = - (top + bottom) / (top - bottom);
        orthographic.matrix[2][3] = - (far + near) / (far - near);

        return orthographic;
    }
}
