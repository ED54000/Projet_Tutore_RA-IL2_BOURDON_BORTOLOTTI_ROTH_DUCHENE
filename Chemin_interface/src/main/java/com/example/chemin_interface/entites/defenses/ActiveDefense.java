package com.example.chemin_interface.entites.defenses;

import com.example.chemin_interface.entites.enemies.Ennemy;
import com.example.chemin_interface.steering_astar.Steering.Vector2D;

public abstract class ActiveDefense extends Defense {

    private double attackSpeed;

    private double health;

    public ActiveDefense(Vector2D position, int health, int damage, double range, double attackSpeed) {
        super(position, damage, range);
        this.attackSpeed = attackSpeed;
        this.health = health;
    }

    public void takeDamage(double damage) {
        health -= damage;
    }

}
