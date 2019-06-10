package killinglewis.math;

public class Vector4f {

    private float x, y, z , w;

    /* Normal X vector. */
    public final static Vector4f xVector = new Vector4f(1, 0, 0, 0);
    /* Normal Y vector. */
    public final static Vector4f yVector = new Vector4f(0, 1, 0,0);
    /* Normal Z vector. */
    public final static Vector4f zVector = new Vector4f(0, 0, 1,0);
    /*Normal W vector. */
    public final static Vector4f wVector = new Vector4f(0,0,0,1);

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public Vector4f add(Vector4f otherVector) {
        return new Vector4f(x + otherVector.getX(), y + otherVector.getY(), z + otherVector.getZ(), w + otherVector.getW());
    }

    public Vector4f dot(Vector4f otherVector) {
        return new Vector4f(x * otherVector.getX(), y * otherVector.getY(), z * otherVector.getZ(), w * otherVector.getW());
    }

}
