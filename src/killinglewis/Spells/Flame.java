package killinglewis.Spells;

import killinglewis.utils.InteractionManager;

public class Flame extends Spell {

    float health;
    float speed;

    public Flame(String name, String figure, String action, float mana, float health, float speed) {
        super(name, figure, action, mana);

        this.health = health;
        this.speed = speed;
    }

    public Flame(float mana, float health, float speed) {
        this("Flame", "Triangle", "Set Lewis on fire", mana, health, speed);
    }

    public Flame() {
        this("Flame", "Triangle", "Set Lewis on fire", 0, 0, 0);
    }

    @Override
    public void cast(InteractionManager interact) {
        interact.reduceHealth(this.health);
        interact.reduceStamina(this.speed);
        interact.reduceMana(this.getManaCost());
    }
}
