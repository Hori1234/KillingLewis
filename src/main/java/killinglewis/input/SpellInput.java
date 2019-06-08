package killinglewis.input;

import java.util.Vector;

public class SpellInput {
    private Vector<Double> SXpos = null;
    private Vector<Double> SYpos = null;

    public SpellInput() {
        SXpos = new Vector<>();
        SYpos = new Vector<>();
    }

    public void addMovement(double x, double y) {
        SXpos.add(x);
        SYpos.add(y);
    }

    public Object[] getSXpos() {
        return SXpos.toArray();
    }

    public Object[] getSYpos() {
        return SYpos.toArray();
    }

    public void clear () {
        SXpos.clear();
        SYpos.clear();
    }
}
