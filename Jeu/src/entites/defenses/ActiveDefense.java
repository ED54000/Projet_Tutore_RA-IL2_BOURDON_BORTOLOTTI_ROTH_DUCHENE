package entites.defenses;

import entites.enemies.Ennemy;

public abstract class ActiveDefense extends Defense {

    // L'ennemi sur lequel la défense est focus
    private Ennemy target;

    public ActiveDefense(double x, double y, double health, int damage, double range, long attackSpeed, String sprite, String name) {
        super(x, y, damage, range, health, sprite, name, attackSpeed);
        this.target = null;
    }

    public Ennemy getTarget() {
        return target;
    }

    public void setTarget(Ennemy target) {
        this.target = target;
    }
}
