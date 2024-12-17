package entites;

import java.util.Random;

public abstract class Entity {

    private double x;
    private double y;
    private String type;
    private double damages;
    private double range;

    public Entity(double x, double y, double damages, double range) {
        this.x = x;
        this.y = y;
        this.damages = damages;
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
                if (TargetType.equals("Plant")) {
                    return 30/100;
                }
                else if (TargetType.equals("Water")) {
                    return -30/100;
                }
                else {
                    return 0;
                }
            case "Water":
                if (TargetType.equals("Fire")) {
                    return 30/100;
                }
                else if (TargetType.equals("Plant")) {
                    return -30/100;
                }
                else {
                    return 0;
                }
            case "Plant":
                if (TargetType.equals("Water")) {
                    return 30/100;
                }
                else if (TargetType.equals("Fire")) {
                    return -30/100;
                }
                else {
                    return 0;
                }
        }
        return 0;
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

    public double getDamages() {
        return damages;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getRange() {
        return range;
    }

    protected void setDamages(double damages) {
        this.damages = damages;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public void setType(String type) {
        this.type = type;
    }
}