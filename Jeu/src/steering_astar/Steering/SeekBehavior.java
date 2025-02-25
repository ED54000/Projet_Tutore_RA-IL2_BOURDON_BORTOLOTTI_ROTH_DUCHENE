package steering_astar.Steering;

import entites.enemies.Ennemy;
import laby.ModeleLabyrinth;

public class SeekBehavior extends Behavior {

    private static final double BASE_SEEK_WEIGHT = 0.5;
    private static final double SLOW_RADIUS = 100;
    private static final double BASE_ARRIVAL_WEIGHT = 25;
    /***
     * constructeur de la classe
     * @param target coordonnees de la cible du comportement
     */
    public SeekBehavior(Vector2D target) {
        this.setTarget(target);
        if (ModeleLabyrinth.getLabyrinth().getUseAstar()){
            this.setWeight(BASE_SEEK_WEIGHT*2);
        } else {
            this.setWeight(BASE_SEEK_WEIGHT);
        }
    }

    /***
     * implementation du comportement seek, qui tend a atteindre une cible
     * @param ennemy l'ennemi sur lequel va etre applique la force
     * @return les coordonnees sur lesquelles va se baser le calcul de la velocite de l'ennemi
     */
    @Override
    public Vector2D calculateForce(Ennemy ennemy) {
        // on détermine si on prends la vitesse normale ou la vitesse réduite
        Vector2D difference = this.getTarget().subtract(ennemy.getPosition());
        double distance = difference.magnitude();
        double baseSpeed = ennemy.getSpeed() * (distance / SLOW_RADIUS);
        double finalSpeed = Math.min(baseSpeed, ennemy.getSpeed());

        // seek
        Vector2D desired = this.getTarget().subtract(ennemy.getPosition()).normalize().scale(finalSpeed);
        desired = desired.subtract(ennemy.getVelocity()).scale(ACCELERATION_DIVISER);

        // si on doit ralentir
        if (finalSpeed == baseSpeed && !(ModeleLabyrinth.getLabyrinth().getUseAstar())){
            desired = desired.scale(BASE_ARRIVAL_WEIGHT);
        }

        return desired;
    }
}



