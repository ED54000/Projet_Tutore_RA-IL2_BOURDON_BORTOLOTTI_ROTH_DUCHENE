package entites.enemies;

import entites.Entity;
import entites.defenses.Defense;

public abstract class Ennemy extends Entity {

    private double health;
    private double speed;
    private int damage;
    private double attackSpeed;
    private double range;
    private double distanceToArrival;
    private String killerType;

    public Ennemy(double x, double y, int health, double speed, int damage, double attackSpeed, double range, double distanceToArrival) {
        super(x, y);
        this.health = health;
        this.speed = speed;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.range = range;
        this.distanceToArrival = distanceToArrival;
        this.killerType = null;
    }

    public void move() {
        // TODO
    }

    public abstract void attack(Defense target);

    public void takeDamage(double damage) {
        health -= damage;
    }

    public double getHealth() {
        return health;
    }

    public double getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }

}
