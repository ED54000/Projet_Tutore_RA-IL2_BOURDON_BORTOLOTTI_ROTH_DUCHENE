package entites;

import java.util.Random;

public class Entity {

    private double x;
    private double y;
    private String type;

    public Entity(double x, double y) {
        this.x = x;
        this.y = y;

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

    public String getType() {
        return type;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}