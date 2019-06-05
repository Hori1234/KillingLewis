package killinglewis.utils;

import java.util.ArrayList;
import killinglewis.Spells.Spell;

/** Class that manages all interactions on Lewis
 *
 */
public class InteractionManager {
    private float health;   // Lewis' health
    private float stamina;  // Lewis' stamina
    private ArrayList<Spell> spells;    // List of spells that can be cast on Lewis

    /** Constructor
     * @param health
     * @param stamina
     */
    public InteractionManager(int health, int stamina) {
        this.health = health;
        this.stamina = stamina;
    }

    /** Default constructor
     * Default settings for health and stamina
     */
    public InteractionManager() {
        this(100, 100);
    }

    public void addSpell(Spell spell) {
        this.spells.add(spell);
    }

    public void removeSpell(Spell spell) {
        this.spells.remove(spell);
    }

    public Spell getSpell(String figure) {
        for (Spell spell : this.spells) {
            if (spell.getFigure() == figure) {
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
        this.health = this.health - reduction;
    }

    public float getStamina() {
        return this.stamina;
    }

    public void setStamina(float stamina) {
        this.stamina = stamina;
    }

    public void reduceStamina(float reduction) {
        this.stamina = this.stamina - reduction;
    }
}
