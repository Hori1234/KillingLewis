package killinglewis.utils;

import java.util.ArrayList;
import killinglewis.Spells.Spell;

public class InteractionManager {
    private int health;
    private int stamina;
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

    public int getHealth() {
        return this.health;
    }

    public int getStamina() {
        return this.stamina;
    }
}
