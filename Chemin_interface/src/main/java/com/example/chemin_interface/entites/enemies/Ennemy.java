package com.example.chemin_interface.entites.enemies;

import com.example.chemin_interface.entites.Entity;
import com.example.chemin_interface.entites.defenses.Defense;

public abstract class Ennemy extends Entity {

    private double speed;
    private double attackSpeed;
    private int distanceToArrival;  //distanceToArrival sera calculé par A* dans le jeu
    private int distanceStartToArrival;
    private String killerType;
    private double health;
    private static int timeSpawn = 0;

    public Ennemy(double x, double y, double health,double speed, int damage, double attackSpeed, double range, int distanceToArrival) {
        super(x, y, damage, range);
        this.speed = speed;
        this.attackSpeed = attackSpeed;
        this.distanceToArrival = distanceToArrival;
        this.distanceStartToArrival = distanceToArrival;
        this.killerType = null;
        this.health = health;
        timeSpawn++;
    }

    public void takeDamage(double damage) {
        health -= damage;
    }

    public void attack(Defense target) {
        if (target != null) {
            target.takeDamage(getDamage()*getBonus(getType(), target.getType()));
        }
    }

    public void move(double secondes) {
        //attendre une seconde
        this.setX(this.getX() + this.speed);
        this.distanceToArrival -= this.speed;
    }

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
}
