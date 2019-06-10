package killinglewis.Spells;

import killinglewis.utils.InteractionManager;

public class GenericSpell extends Spell {

    private int health;
    private int speed;
    private int stamina;

    public GenericSpell(String name, String figure, String action, int mana, int health, int speed, int stamina) {
        super(name, figure, action, mana);

        this.health = health;
        this.speed = speed;
        this.stamina = stamina;
    }

    @Override
    public void cast(InteractionManager interact) {

    }
}
