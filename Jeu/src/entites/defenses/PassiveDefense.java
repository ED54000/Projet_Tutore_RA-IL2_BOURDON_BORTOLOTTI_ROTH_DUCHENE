package entites.defenses;

import entites.enemies.Ennemy;
import laby.ModeleLabyrinth;

public abstract class PassiveDefense extends Defense {

    public PassiveDefense(double x, double y, int damage, double range, double health, String sprite, String name) {
            super(x, y, damage, range, health, sprite, name);
        }

    /**
     * Attaque un ennemi
     * @param target l'ennemi à attaquer
     */
    @Override
    public void attack(Ennemy target) {
        target.takeDamage(getDamages()*getBonus(getType(), target.getType()));
    }
}
