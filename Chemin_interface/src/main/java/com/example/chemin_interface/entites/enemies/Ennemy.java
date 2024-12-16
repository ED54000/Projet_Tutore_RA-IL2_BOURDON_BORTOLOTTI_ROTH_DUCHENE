package com.example.chemin_interface.entites.enemies;

import com.example.chemin_interface.entites.Entity;
import com.example.chemin_interface.entites.defenses.Defense;
import com.example.chemin_interface.steering_astar.Steering.Behavior;
import com.example.chemin_interface.steering_astar.Steering.Vector2D;

public abstract class Ennemy extends Entity {

    private double attackSpeed;
    private int distanceToArrival;  //distanceToArrival sera calculé par A* dans le jeu
    private int distanceStartToArrival;
    private String killerType;
    private double health;
    private static int timeSpawn = 0;
    private double maxSpeed;
    private Vector2D velocity;
    private Behavior behavior;

    public Ennemy(Vector2D position, int damage, double range, double health,  double attackSpeed, int distanceToArrival, double maxSpeed) {
        super(position, damage, range);
        this.health = health;
        this.attackSpeed = attackSpeed;
        this.distanceToArrival = distanceToArrival;
        this.distanceStartToArrival = distanceToArrival;
        this.killerType = null;
        timeSpawn++;
        this.maxSpeed = maxSpeed;
        this.velocity = new Vector2D(0, 0);
        this.behavior = null;
    }

    public void takeDamage(double damage) {
        health -= damage;
    }

    public void attack(Defense target) {
        if (target != null) {
            target.takeDamage(getDamage()*getBonus(getType(), target.getType()));
        }
    }

    /***
     * methode definissant la nouvelle position de l'agent grace a sa velocite, calculee selon le
     * comportement de l'agent
     */
    public void update() {
        if (behavior != null) {
            Vector2D steeringForce = behavior.calculateForce(this);
            velocity = velocity.add(steeringForce).normalize().scale(maxSpeed);
            position = position.add(velocity);
        }
    }

//    public void move(double secondes) {
//        //attendre une seconde
//        this.getPosition().setX(this.getPosition().getX() + this.speed);
//        this.distanceToArrival -= this.speed;
//    }

    public int getDistanceToArrival() {
        return distanceToArrival;
    }

    public int getDistanceStartToArrival() {
        return distanceStartToArrival;
    }

    public int getTimeSpawn() {
        return timeSpawn;
    }

    public void setTimeSpawn(int i) {
        timeSpawn = i;
    }

    public Vector2D getVelocity() { return velocity; }

    public double getMaxSpeed() { return maxSpeed; }

    public Behavior getBehavior() { return behavior; }

    public void setBehavior(Behavior behavior) {
        this.behavior = behavior;
    }
}
