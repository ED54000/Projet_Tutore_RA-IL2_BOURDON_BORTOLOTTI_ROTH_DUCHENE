package entites.defenses;

import entites.enemies.Ennemy;

public abstract class ActiveDefense extends Defense {

    private double attackSpeed;

    private double health;

    // Indique si la défense est actuellement focus sur un ennemi
    private boolean isFocused;

    public ActiveDefense(double x, double y, int health, int damage, double range, double attackSpeed) {
        super(x, y, damage, range);
        this.attackSpeed = attackSpeed;
        this.health = health;
        this.isFocused = false;
    }

    /**
     * Attaque un ennemi
     * @param target l'ennemi à attaquer
     */
    @Override
    public void attack(Ennemy target) {
        // On attaque l'ennemi
        target.takeDamage(this.getDamages()*getBonus(getType(), target.getType()) + this.getDamages());
    }

    public void takeDamage(double damage) {
        health -= damage;
    }

    public boolean getIsFocused() {
        return isFocused;
    }

}
