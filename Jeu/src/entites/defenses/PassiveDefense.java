package entites.defenses;

public abstract class PassiveDefense extends Defense {

    // Si la defense a deja attaqué
    private boolean attacked = false;

    public PassiveDefense(double x, double y, int damage, double range, double health, String sprite, String name) {
        super(x, y, damage, range, health, sprite, name, 100);
    }

    /**
     * Appliques si la défense a déjà attaqué ou non
     * @param attacked true si la défense a déjà attaqué, false sinon
     */
    public void setAttacked(boolean attacked) {
        this.attacked = attacked;
    }

    /**
     * Retourne si la défense a déjà attaqué ou non
     * @return true si la défense a déjà attaqué, false sinon
     */
    public boolean isAttacked() {
        return this.attacked;
    }

}
