package entites.defenses;

import entites.Entity;
import entites.enemies.Ennemy;
import laby.views.ViewLabyrinth;
import steering_astar.Steering.Vector2D;

import static laby.ModeleLabyrinth.getTailleCase;

public abstract class Defense extends Entity {

    private double health;
    private boolean isDead = false;
    private String name;

    public Defense(double x, double y, int damage, double range, double health, String sprite, String name) {
        super(new Vector2D(x, y), damage, range, sprite);
        this.health = health;
        this.name = name;
    }

    public abstract void attack(Ennemy target);

//    /**
//     * Vérifie si un ennemi est dans la portée de la défense
//     *
//     * @param target l'ennemi à vérifier
//     * @return true si l'ennemi est dans la portée de la défense, false sinon
//     */
//    public boolean isInRange(Ennemy target) {
//        double targetX = target.getPosition().getX() / ViewLabyrinth.getTailleCase();
//        double targetY = target.getPosition().getY() / ViewLabyrinth.getTailleCase();
//        double defenseX = this.getPosition().getX();
//        double defenseY = this.getPosition().getY();
//
//        // Calculer la distance au carré entre les positions
//        double deltaX = targetX - defenseX;
//        double deltaY = targetY - defenseY;
//        double distanceSquared = deltaX * deltaX + deltaY * deltaY;
//
//
//        double rangeInPixels = this.getRange();
//
//        // Vérification si l'ennemi est dans la portée
//        return distanceSquared <= rangeInPixels * rangeInPixels;
//    }

    public double getHealth() {
        return this.health;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Prendre des dégâts
     * @param damage les dégâts à prendre
     */
    public void takeDamage(double damage) {
        health -= damage;
        if (this.getHealth() <= 0) {
            this.setDead(true);
        }
    }

    protected void setHealth(double health) {
        this.health = health;
    }

    public void setDead(boolean b) {
        this.isDead = b;
    }

    public boolean isDead() {
        return this.isDead;
    }
}

