package steering_astar.Steering;

import entites.enemies.Ennemy;
import laby.ModeleLabyrinth;

public class SeekBehavior extends Behavior {

    private static final double BASE_SEEK_WEIGHT = 0.5;

    /***
     * constructeur de la classe
     * @param target coordonnees de la cible du comportement
     */
    public SeekBehavior(Vector2D target) {
        this.setTarget(target);
        this.setWeight(BASE_SEEK_WEIGHT);
    }

    /***
     * implementation du comportement seek, qui tend a atteindre une cible
     * @param ennemy l'ennemi sur lequel va etre applique la force
     * @return les coordonnees sur lesquelles va se baser le calcul de la velocite de l'ennemi
     */
    @Override
    public Vector2D calculateForce(Ennemy ennemy) {
        Vector2D desired = this.getTarget().subtract(ennemy.getPosition()).normalize().scale(ennemy.getMaxSpeed());
        desired = desired.subtract(ennemy.getVelocity()).scale(ACCELERATION_DIVISER);
        if (!ModeleLabyrinth.getUseAstar()){
            desired = desired.scale(this.getWeight());
        }
        return desired;
    }
}



