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

