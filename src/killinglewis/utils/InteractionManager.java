package killinglewis.utils;

import java.util.ArrayList;

import killinglewis.Spells.Flame;
import killinglewis.Spells.Spell;

/** Class that manages all interactions on Lewis
 *
 */
public class InteractionManager {
    private float health;   // Lewis' health
    private float stamina;  // Lewis' stamina
    private float mana;     // Players mana
    private float obstructCost = 0.2f;
    private int obstruct = 0;   // nr of obstructions
    private ArrayList<Spell> spells = new ArrayList<>();    // List of spells that can be cast on Lewis
    private static float HEALTH_REGEN = 0.0001f;
    private static float STAMINA_REGEN = 0.001f;
    private static float MANA_REGEN = 0.0005f;

    /** Constructor
     * @param health
     * @param stamina
     * @param mana
     */
    public InteractionManager(float health, float stamina, float mana) {
        this.health = health;
        this.stamina = stamina;
        this.mana = mana;

    }

    /** Default constructor
     * Default settings for health and stamina
     */
    public InteractionManager() {
        this(1f, 1f,1f);
    }

    public void addSpell(Spell spell) {
        this.spells.add(spell);
    }

    public void removeSpell(Spell spell) {
        this.spells.remove(spell);
    }

    public Spell getSpell(String name) {
        for (Spell spell : this.spells) {
            if (spell.getName().equals(name)) {
                return spell;
            }
        }
        return null;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getHealth() {
        return this.health;
    }

    public void reduceHealth(float reduction) {
        this.health = (this.health - reduction > 0) ? this.health - reduction : 0f;
    }

    public float getStamina() {
        return this.stamina;
    }

    public void setStamina(float stamina) {
        this.stamina = stamina;
    }

    public void reduceStamina(float reduction) {
        this.stamina = (this.stamina - reduction > 0) ? this.stamina - reduction : 0f;
    }

    public void setMana(float mana) {
        this.mana = mana;
    }

    public float getMana() {
        return this.mana;
    }

    public void reduceMana(float reduction) {
        this.mana = (this.mana - reduction > 0) ? this.mana - reduction : 0f;
    }

    public boolean enoughMana(float cost) {
        return this.mana - cost > 0f - cost / 10;
    }

    public int getObstruct() {
        return this.obstruct;
    }

    public void addObstruct() {
        this.obstruct = this.obstruct + 1;
    }

    public void reduceObstruct() {
        this.obstruct = (this.obstruct > 0) ? this.obstruct - 1 : 0;
    }

    public boolean enoughObstruct() {
        return this.obstruct > 0;
    }

    public float obstructionCost() {
        return this.obstructCost;
    }

    public void regenerate() {
        this.health += HEALTH_REGEN;
        this.mana += MANA_REGEN;
        this.stamina += STAMINA_REGEN;

        if (this.health >= 1f) this.health = 1f;
        if (this.mana >= 1f) this.mana = 1f;
        if (this.stamina >= 1f) this.stamina = 1f;
    }
}
