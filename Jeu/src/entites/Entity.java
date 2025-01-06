package entites;

import entites.enemies.Ennemy;
import laby.views.ViewLabyrinth;
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

    /**
     * Vérifie si une entite est dans la portée de l'entite courante
     *
     * @param target l'entite à vérifier
     * @return true si l'entite à vérifier est dans la portée de l'entite courante, false sinon
     */
    public boolean isInRange(Entity target) {
        double targetX = target.getPosition().getX() / ViewLabyrinth.getTailleCase();
        double targetY = target.getPosition().getY() / ViewLabyrinth.getTailleCase();
        double entityX = this.getPosition().getX();
        double entityY = this.getPosition().getY();

        // Calculer la distance au carré entre les positions
        double deltaX = targetX - entityX;
        double deltaY = targetY - entityY;
        double distanceSquared = deltaX * deltaX + deltaY * deltaY;


        double rangeInPixels = this.getRange();

        // Vérification si l'ennemi est dans la portée
        return distanceSquared <= rangeInPixels * rangeInPixels;
    }

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