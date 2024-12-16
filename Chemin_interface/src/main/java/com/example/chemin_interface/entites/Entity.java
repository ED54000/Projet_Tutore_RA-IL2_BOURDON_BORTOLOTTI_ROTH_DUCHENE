package com.example.chemin_interface.entites;

import java.util.Random;
import com.example.chemin_interface.steering_astar.Steering.Vector2D;

public abstract class Entity {

    protected Vector2D position;
    private String type;
    private int damage;
    private double range;

    public Entity(Vector2D position, int damage, double range) {
        this.position = position;
        this.damage = damage;
        this.range = range;

        //génère un type aléatoire
        int randomNumber = (int) (Math.random() * 3)+1;
        switch (randomNumber) {
            case 1:
                this.type = "Fire";
                break;
            case 2:
                this.type = "Water";
                break;
            case 3:
                this.type = "Wood";
                break;
        }
    }

    //retourne le bonus de dégâts en fonction des types
    public double getBonus(String AttackerType, String TargetType) {
        switch (AttackerType) {
            case "Fire":
                if (TargetType.equals("Wood")) {
                    return 30/100;
                }
                if (TargetType.equals("Water")) {
                    return -30/100;
                }
                break;
            case "Water":
                if (TargetType.equals("Fire")) {
                    return 30/100;
                }
                if (TargetType.equals("Wood")) {
                    return -30/100;
                }
                break;
            case "Wood":
                if (TargetType.equals("Water")) {
                    return 30/100;
                }
                if (TargetType.equals("Fire")) {
                    return -30/100;
                }
                break;
        }
        return 1;
    }

    public abstract void takeDamage(double damage);

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public int getDamage() {
        return damage;
    }

    public double getRange() {
        return range;
    }
}