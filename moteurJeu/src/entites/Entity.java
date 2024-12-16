package entites;

import java.util.Random;

public abstract class Entity {

    private double x;
    private double y;
    private String type;
    private int damage;
    private double range;

    public Entity(double x, double y, int damage, double range) {
        this.x = x;
        this.y = y;
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

    public String getType() {
        return type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getDamage() {
        return damage;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getRange() {
        return range;
    }
}