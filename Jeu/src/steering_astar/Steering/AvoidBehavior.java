package steering_astar.Steering;

import entites.enemies.Ennemy;
import laby.ModeleLabyrinth;

public class AvoidBehavior extends Behavior {

    private static final double MAX_SEE_AHEAD = 1.15;
    private static final double BASE_AVOID_WEIGHT = 3;

    /***
     * constructeur de la classe
     * @param target coordonnees de la cible du comportement
     */
    public AvoidBehavior(Vector2D target) {
        this.setTarget(target);
        this.setWeight(BASE_AVOID_WEIGHT);
    }

    /***
     * implementation du comportement avoid, qui tend a eviter les obstacles
     * @param ennemy l'ennemi sur lequel va etre applique la force
     * @return les coordonnees sur lesquelles va se baser le calcul de la velocite de l'ennemi
     */
    @Override
    public Vector2D calculateForce(Ennemy ennemy) {
        Vector2D desired = ennemy.getVelocity().subtract(this.getTarget());
        (desired.normalize()).scale(ACCELERATION_DIVISER);
        if (!ModeleLabyrinth.getUseAstar()){
            desired.scale(this.getWeight());
        }
        return desired.normalize();
    }

    public static double getMAX_SEE_AHEAD(){
        return MAX_SEE_AHEAD;
    }
}
