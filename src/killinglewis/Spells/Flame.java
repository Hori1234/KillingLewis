package killinglewis.Spells;

import killinglewis.utils.InteractionManager;

public class Flame extends Spell {

    int health;
    int speed;

    public Flame(String name, String figure, String action, int mana, int health, int speed) {
        super(name, figure, action, mana);

        this.health = health;
        this.speed = speed;
    }

    public Flame(int mana, int health, int speed) {
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
