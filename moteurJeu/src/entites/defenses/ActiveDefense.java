package entites.defenses;

import entites.enemies.Ennemy;

public abstract class ActiveDefense extends Defense {

    private double attackSpeed;

    private double health;

    public ActiveDefense(double x, double y, int health, int damage, double range, double attackSpeed) {
        super(x, y, damage, range);
        this.attackSpeed = attackSpeed;
        this.health = health;
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

}
