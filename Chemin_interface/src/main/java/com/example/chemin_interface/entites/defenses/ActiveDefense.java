package com.example.chemin_interface.entites.defenses;

import com.example.chemin_interface.entites.enemies.Ennemy;

public abstract class ActiveDefense extends Defense {

    private double attackSpeed;

    private double health;

    public ActiveDefense(double x, double y, int health, int damage, double range, double attackSpeed) {
        super(x, y, damage, range);
        this.attackSpeed = attackSpeed;
        this.health = health;
    }

    public void takeDamage(double damage) {
        health -= damage;
    }

}
