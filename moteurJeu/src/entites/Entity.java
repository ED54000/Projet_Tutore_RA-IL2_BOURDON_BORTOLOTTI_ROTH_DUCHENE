package entites;

import steering_astar.Steering.Vector2D;

public abstract class Entity {

    protected Vector2D position;
    private String type;
    private double damages;
    private double range;

    public Entity(Vector2D position, double damages, double range) {
        this.position = position;
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
                this.type = "Plant";
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
                    return 1;
                }
            case "Water":
                if (TargetType.equals("Fire")) {
                    return 30/100;
                }
                else if (TargetType.equals("Plant")) {
                    return -30/100;
                }
                else {
                    return 1;
                }
            case "Plant":
                if (TargetType.equals("Water")) {
                    return 30/100;
                }
                else if (TargetType.equals("Fire")) {
                    return -30/100;
                }
                else {
                    return 1;
                }
        }
        return 0;
    }

    public abstract void takeDamage(double damage);

    public String getType() {
        return type;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public double getDamages() {
        return damages;
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