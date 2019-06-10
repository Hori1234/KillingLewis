package killinglewis.Spells;

import killinglewis.utils.InteractionManager;

public class Hamer extends Spell {

    int health;

    public Hamer(String name, String figure, String action, int mana, int health) {
        super(name, figure, action, mana);

        this.health = health;
    }

    public Hamer(int mana, int health) {
        this("Hamer", "Square", "Hit Lewis", mana, health);
    }

    public Hamer() {
        this("Hamer", "Square", "Hit Lewis", 0, 0);
    }

    @Override
    public void cast(InteractionManager interact) {
        interact.reduceHealth(this.health);
        interact.reduceMana(this.getManaCost());
    }
}
