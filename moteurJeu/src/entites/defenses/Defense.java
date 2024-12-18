package entites.defenses;

import entites.Entity;
import entites.enemies.Ennemy;
import steering_astar.Steering.Vector2D;

public abstract class Defense extends Entity {

    public Defense(double x, double y, int damage, double range) {
        super(new Vector2D(x,y), damage, range);
    }

    public abstract void attack(Ennemy target);

    /**
     * Vérifie si un ennemi est dans la portée de la défense
     * @param target l'ennemi à vérifier
     * @return true si l'ennemi est dans la portée de la défense, false sinon
     */
    public boolean isInRange(Ennemy target) {
        // On détecte si un ennemi est dans la portée de la défense avec une formule de distance euclidienne
        if (((target.getPosition().getX() - this.getPosition().getX()) * (target.getPosition().getX() - this.getPosition().getX()) +
                (target.getPosition().getY() - this.getPosition().getY()) * (target.getPosition().getY() - this.getPosition().getY()) <= this.getRange() * this.getRange())) {
            return true;
        }
        // Sinon
        else
            return false;
    }
}

