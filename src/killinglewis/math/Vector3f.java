package killinglewis.math;

import static java.lang.Math.sqrt;

public class Vector3f {
    /* Vector components. */
    private float x, y, z;

    /* Normal X vector. */
    public final static Vector3f xVector = new Vector3f(1, 0, 0);
    /* Normal Y vector. */
    public final static Vector3f yVector = new Vector3f(0, 1, 0);
    /* Normal Z vector. */
    public final static Vector3f zVector = new Vector3f(0, 0, 1);

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f (Vector4f vector4f) {
        this.x = vector4f.getX() / vector4f.getW();
        this.y = vector4f.getY() / vector4f.getW();
        this.z = vector4f.getZ() / vector4f.getW();
    }

    /**
     * Returns the X coordinate of the vector.
     *
     * @return X coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * Returns the Y coordinate of the vector.
     *
     * @return Y coordinate
     */
    public float getY() {
        return y;
    }

    /**
     * Returns the Z coordinate of the vector.
     *
     * @return Z coordinate
     */
    public float getZ() {
        return z;
    }

    /**
     * Retrieves the length of the vector.
     *
     * @return length of vector
     */
    public float getLength() {
        return (float) sqrt(x * x + y * y + z * z);
    }

    /**
     * Calculates and returns the cross product between this vector and another one.
     *
     * @param otherVector the other vector
     * @return the cross product result
     */
    public Vector3f cross(Vector3f otherVector) {
        float resultX = y * otherVector.getZ() - z * otherVector.getY();
        float resultY = z * otherVector.getX() - x * otherVector.getZ();
        float resultZ = x * otherVector.getY() - y * otherVector.getX();
        return new Vector3f(resultX, resultY, resultZ);
    }

    /**
     * Calculates and returns the dot product between this vector and another one.
     *
     * @param otherVector the other vector
     * @return the dot product result
     */
    public Vector3f dot(Vector3f otherVector) {
        return new Vector3f(x * otherVector.getX(), y * otherVector.getY(), z * otherVector.getZ());
    }

    /**
     * Returns a scaled version of this vector.
     *
     * @param scale the scale
     * @return scaled vector
     */
    public Vector3f scale(float scale) {
        return new Vector3f(x * scale, y * scale, z * scale);
    }

    /**
     * Calculates and returns the addition of another vector to this vector.
     *
     * @param otherVector the other vector
     * @return the addition result
     */
    public Vector3f add(Vector3f otherVector) {
        return new Vector3f(x + otherVector.getX(), y + otherVector.getY(), z + otherVector.getZ());
    }

    /**
     * Calculates and returns the subtraction of another vector from this vector.
     *
     * @param otherVector the other vector
     * @return the addition result
     */
    public Vector3f subtract(Vector3f otherVector) {
        return new Vector3f(x - otherVector.getX(), y - otherVector.getY(), z - otherVector.getZ());
    }

    public Vector3f normalise () {
        float len = this.getLength();
        this.x = x / len;
        this.y = y / len;
        this.z = z / len;
        return this;
    }

    public Vector3f negate() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
