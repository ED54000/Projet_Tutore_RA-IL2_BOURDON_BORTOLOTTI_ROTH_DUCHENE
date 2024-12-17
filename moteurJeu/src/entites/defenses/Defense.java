package entites.defenses;

import entites.Entity;
import entites.enemies.Ennemy;

public abstract class Defense extends Entity {

    public Defense(double x, double y, int damage, double range) {
        super(x, y, damage, range);
    }

    public abstract void attack(Ennemy target);

    /**
     * Vérifie si un ennemi est dans la portée de la défense
     * @param target l'ennemi à vérifier
     * @return true si l'ennemi est dans la portée de la défense, false sinon
     */
    public boolean isInRange(Ennemy target) {
        // On détecte si un ennemi est dans la portée de la défense avec une formule de distance euclidienne
        if (((target.getX() - this.getX()) * (target.getX() - this.getX()) +
                (target.getY() - this.getY()) * (target.getY() - this.getY()) <= this.getRange() * this.getRange())) {
            return true;
        }
        // Sinon
        else
            return false;
    }
}

