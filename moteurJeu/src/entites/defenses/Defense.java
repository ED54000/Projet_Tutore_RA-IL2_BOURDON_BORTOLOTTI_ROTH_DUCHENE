package entites.defenses;

import entites.Entity;
import entites.enemies.Ennemy;
import laby.views.ViewLabyrinth;
import steering_astar.Steering.Vector2D;

public abstract class Defense extends Entity {

    public Defense(double x, double y, int damage, double range) {
        super(new Vector2D(x, y), damage, range);
    }

    public abstract void attack(Ennemy target);

    /**
     * Vérifie si un ennemi est dans la portée de la défense
     *
     * @param target l'ennemi à vérifier
     * @return true si l'ennemi est dans la portée de la défense, false sinon
     */
    public boolean isInRange(Ennemy target) {
        double targetX = target.getPosition().getX() / ViewLabyrinth.getTailleCase();
        double targetY = target.getPosition().getY() / ViewLabyrinth.getTailleCase();
        double defenseX = this.getPosition().getX();
        double defenseY = this.getPosition().getY();

        // Calculer la distance au carré entre les positions
        double deltaX = targetX - defenseX;
        double deltaY = targetY - defenseY;
        double distanceSquared = deltaX * deltaX + deltaY * deltaY;


        double rangeInPixels = this.getRange();

        // Vérification si l'ennemi est dans la portée
        return distanceSquared <= rangeInPixels * rangeInPixels;
    }

}

