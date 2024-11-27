package entites.defenses;

import entites.Entity;
import entites.enemies.Ennemy;

public abstract class Defense extends Entity {

    private double health;
    private int damage;
    private double range;

    public Defense(double x, double y, int health, int damage, double range) {
        super(x, y);
        this.health = health;
        this.damage = damage;
        this.range = range;
    }

    public abstract void attack(Ennemy target);

    public void takeDamage(double damage) {
        health -= damage;
    }

    public double getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public double getRange() {
        return range;
    }

}

