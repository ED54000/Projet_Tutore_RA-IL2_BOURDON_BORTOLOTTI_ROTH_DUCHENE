package entites.enemies;

import entites.Entity;
import entites.defenses.Defense;

public abstract class Ennemy extends Entity {

    private double speed;
    private double attackSpeed;
    private double distanceToArrival;
    private String killerType;
    private double health;

    public Ennemy(double x, double y, double health,double speed, int damage, double attackSpeed, double range, double distanceToArrival) {
        super(x, y, damage, range);
        this.speed = speed;
        this.attackSpeed = attackSpeed;
        this.distanceToArrival = distanceToArrival;
        this.killerType = null;
        this.health = health;
    }

    public void move(double[] next) {
        // TODO
    }

    public void takeDamage(double damage) {
        health -= damage;
    }

    public abstract void attack(Defense target);

    public void update(double secondes) {
        this.setX(this.getX() + this.speed);
    }
}
