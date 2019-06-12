package killinglewis.Spells;

import killinglewis.utils.InteractionManager;

public class Soak extends Spell {

    float stamina;

    public Soak(String name, String figure, String action, float mana, float stamina) {
        super(name, figure, action, mana);

        this.stamina = stamina;
    }

    public Soak(float mana, float stamina) {
        this("Soak", "Circle", "Soak Lewis", mana, stamina);
    }

    public Soak() {
        this("Soak", "Circle", "Soak Lewis", 0, 0);
    }

    @Override
    public void cast(InteractionManager interact) {
        interact.reduceStamina(this.stamina);
        interact.reduceMana(this.getManaCost());
    }
}
