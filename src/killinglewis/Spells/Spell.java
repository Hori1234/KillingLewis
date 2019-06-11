package killinglewis.Spells;

import killinglewis.utils.InteractionManager;

/** Generic representation of a spell
 * @author Joris
 */
public abstract class Spell {

    private String name; // name of the spell (for display)
    private String figure; // figure that has to be drawn
    private String action; // description of the spell effect

    private int mana; // mana cost to cast spell

    public Spell(String name, String figure, String action, int mana) {
        this.name = name;
        this.figure = figure;
        this.action = action;
        this.mana = mana;
    }

    public abstract void cast(InteractionManager interact);

    public String getName() {
        return this.name;
    }

    public String getFigure() {
        return this.figure;
    }

    public String getAction() {
        return this.action;
    }

    public int getManaCost() {
        return this.mana;
    }
}
