package entites.defenses;

import entites.Entity;
import steering_astar.Steering.Vector2D;

public abstract class Defense extends Entity {

//    private double health;
//    private boolean isDead = false;
//    private String name;

    public Defense(double x, double y, int damage, double range, double health, String sprite, String name, double attackSpeed) {
        super(new Vector2D(x, y), damage, range, sprite, health, name, attackSpeed);
    }

//    public abstract void attack(Ennemy target);

//    public double getHealth() {
//        return this.health;
//    }
//
//    public String getName() {
//        return this.name;
//    }

//    /**
//     * Prendre des dégâts
//     * @param damage les dégâts à prendre
//     */
//    public void takeDamage(double damage) {
//        health -= damage;
//        if (this.getHealth() <= 0) {
//            this.setDead(true);
//        }
//    }

//    //TODO : remettre en ptroected quand l'évolution des défenses sera faite
//    public void setHealth(double health) {
//        this.health = health;
//    }

//    public void setDead(boolean b) {
//        this.isDead = b;
//    }
//
//    public boolean isDead() {
//        return this.isDead;
//    }
}

