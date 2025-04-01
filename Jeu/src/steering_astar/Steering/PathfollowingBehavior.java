package steering_astar.Steering;

import entites.enemies.Ennemy;

import java.util.ArrayList;

/***
 * classe implémentant le comportement de suivi de chemin.
 * Ce comportement fonctionne a l'aide d'une liste de coordonnees, qui
 * vont etre parcourues avec le comportement seek
 */
public class PathfollowingBehavior extends Behavior {

    private static final double BASE_PATHFOLLOWING_WEIGHT = 1;
    private ArrayList<Vector2D> checkpoints;
    private Vector2D currentCheckpoint;
    private int currentCheckpointIndex;

    /***
     * constructeur de la classe
     * @param checkpoints liste des coordonnees qui devront etre parcourues
     */
    public PathfollowingBehavior(ArrayList<Vector2D> checkpoints) {
        this.checkpoints = checkpoints;
        this.currentCheckpoint = this.checkpoints.getFirst();
        this.currentCheckpointIndex = 0;
        this.setTarget(currentCheckpoint);
        this.setWeight(BASE_PATHFOLLOWING_WEIGHT);
    }

    /***
     * implementation du comportement de suivi de chemin,
     * qui fonctionne en appelant le comportement seek sur chaque point consecutivement
     * @param ennemy l'ennemi sur lequel va etre applique la force
     * @return les coordonnees sur lesquelles va se baser le calcul de la velocite de l'ennemi
     */
    @Override
    public Vector2D calculateForce(Ennemy ennemy) {
        SeekBehavior seek = new SeekBehavior(currentCheckpoint);
        Vector2D res = seek.calculateForce(ennemy);
        if (ennemy.getPosition().distanceTo(currentCheckpoint) < Behavior.getTargetRadius()) {
            if (currentCheckpointIndex != checkpoints.size()-1) {
                currentCheckpointIndex++;
            }
            currentCheckpoint = checkpoints.get(currentCheckpointIndex);
        }
        return res;
    }

    public void setCheckpoints(ArrayList<Vector2D> checkpoints) {
        this.checkpoints = checkpoints;
        this.currentCheckpoint = this.checkpoints.getFirst();
        this.currentCheckpointIndex = 0;
        this.setTarget(currentCheckpoint);
    }

    public ArrayList<Vector2D> getCheckpoints() {
        return checkpoints;
    }

    public void setCurrentCheckpoint(Vector2D currentCheckpoint) {
        this.currentCheckpoint = currentCheckpoint;
    }
}
