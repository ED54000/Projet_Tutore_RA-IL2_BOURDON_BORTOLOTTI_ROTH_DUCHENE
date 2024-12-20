package entites.defenses;

import entites.enemies.Ennemy;
import laby.ModeleLabyrinth;

public abstract class PassiveDefense extends Defense {

    public PassiveDefense(double x, double y, int damage, double range, double health) {
            super(x, y, damage, range, health);
        }

    /**
     * Attaque un ennemi
     * @param target l'ennemi à attaquer
     */
    @Override
    public void attack(Ennemy target) {
        target.takeDamage(getDamages()*getBonus(getType(), target.getType()));
    }

    /**
     * Prendre des dégâts
     * @param damage les dégâts à prendre
     */
    public void takeDamage(double damage) {
        this.setHealth(this.getHealth()- damage);
        if (this.getHealth() <= 0) {
            this.setDead(true);
        }
    }
}
