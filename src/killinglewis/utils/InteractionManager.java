package killinglewis.utils;

import java.util.ArrayList;
import killinglewis.Spells.Spell;

public class InteractionManager {
    private float health;
    private float stamina;
    private ArrayList<Spell> spells;

    public InteractionManager(int health, int stamina) {
        this.health = health;
        this.stamina = stamina;
    }

    public InteractionManager() {
        this(100, 100);
    }

    public void addSpell(Spell spell) {
        spells.add(spell);
    }

    public void removeSpell(Spell spell) {
        spells.remove(spell);
    }

    public float getHealth() {
        return this.health;
    }

    public float getStamina() {
        return this.stamina;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void reduceHealth(float reduction) {
        this.health = health - reduction;
    }
}
