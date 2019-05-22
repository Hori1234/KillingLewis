package killinglewis.Spells;

public class Soak extends Spell {

    int stamina;

    public Soak(String name, String figure, String action, int mana, int stamina) {
        super(name, figure, action, mana);

        this.stamina = stamina;
    }

    public Soak(int mana, int stamina) {
        this("Soak", "Circle", "Soak Lewis", mana, stamina);
    }

    public Soak() {
        this("Soak", "Circle", "Soak Lewis", 0, 0);
    }

    @Override
    public void cast() {

    }
}
