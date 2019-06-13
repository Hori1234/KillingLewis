package killinglewis.Spells;

import killinglewis.utils.InteractionManager;

public class Flame extends Spell {

    float health;
    float stamina;

    public Flame(String name, String figure, String action, float mana, float health, float stamina) {
        super(name, figure, action, mana);

        this.health = health;
        this.stamina = stamina;
    }

    public Flame(float mana, float health, float stamina) {
        this("Flame", "Triangle", "Set Lewis on fire", mana, health, stamina);
    }

    public Flame() {
        this("Flame", "Triangle", "Set Lewis on fire", 0, 0, 0);
    }

    @Override
    public void cast(InteractionManager interact) {
        interact.reduceHealth(this.health);
        interact.reduceStamina(this.stamina);
        interact.reduceMana(this.getManaCost());
    }
}
