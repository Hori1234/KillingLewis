package killinglewis.math;
import java.util.Vector;

import static java.lang.Math.*;

public class Vector3d {
    /* Vector components. */
    private double x, y, z;

    /* Normal X vector. */
    public final static Vector3d xVector = new Vector3d(1, 0, 0);
    /* Normal Y vector. */
    public final static Vector3d yVector = new Vector3d(0, 1, 0);
    /* Normal Z vector. */
    public final static Vector3d zVector = new Vector3d(0, 0, 1);

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the X coordinate of the vector.
     *
     * @return X coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the Y coordinate of the vector.
     *
     * @return Y coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the Z coordinate of the vector.
     *
     * @return Z coordinate
     */
    public double getZ() {
        return z;
    }

    /**
     * Retrieves the length of the vector.
     *
     * @return length of vector
     */
    public double getLength() {
        return sqrt(x * x + y * y + z * z);
    }

    /**
     * Calculates and returns the cross product between this vector and another one.
     *
     * @param otherVector the other vector
     * @return the cross product result
     */
    public Vector3d cross(Vector3d otherVector) {
        double resultX = y * otherVector.getZ() - z * otherVector.getY();
        double resultY = z * otherVector.getX() - x * otherVector.getZ();
        double resultZ = x * otherVector.getY() - y * otherVector.getX();
        return new Vector3d(resultX, resultY, resultZ);
    }

    /**
     * Calculates and returns the dot product between this vector and another one.
     *
     * @param otherVector the other vector
     * @return the dot product result
     */
    public Vector3d dot(Vector3d otherVector) {
        return new Vector3d(x * otherVector.getX(), y * otherVector.getY(), z * otherVector.getZ());
    }

    /**
     * Returns a scaled version of this vector.
     *
     * @param scale the scale
     * @return scaled vector
     */
    public Vector3d scale(double scale) {
        return new Vector3d(x * scale, y * scale, z * scale);
    }

    /**
     * Calculates and returns the addition of another vector to this vector.
     *
     * @param otherVector the other vector
     * @return the addition result
     */
    public Vector3d add(Vector3d otherVector) {
        return new Vector3d(x + otherVector.getX(), y + otherVector.getY(), z + otherVector.getZ());
    }

    /**
     * Calculates and returns the subtraction of another vector from this vector.
     *
     * @param otherVector the other vector
     * @return the addition result
     */
    public Vector3d subtract(Vector3d otherVector) {
        return new Vector3d(x - otherVector.getX(), y - otherVector.getY(), z - otherVector.getZ());
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
