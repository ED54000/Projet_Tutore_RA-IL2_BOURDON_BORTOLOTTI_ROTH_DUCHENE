package com.example.steering_astar.Steering;

import java.util.ArrayList;

/***
 * classe implémentant le comportement de suivi de chemin.
 * Ce comportement fonctionne a l'aide d'une liste de coordonnees, qui
 * vont etre parcourues avec le comportement seek
 */
public class PathfollowingBehavior extends Behavior {

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
    }

    /***
     * implementation du comportement de suivi de chemin,
     * qui fonctionne en appelant le comportement seek sur chaque point consecutivement
     * @param agent l'agent sur lequel va etre applique la force
     * @return les coordonnees sur lesquelles va se baser le calcul de la velocite de l'agent
     */
    @Override
    public Vector2D calculateForce(Agent agent) {
        SeekBehavior seek = new SeekBehavior(currentCheckpoint);
        Vector2D res = seek.calculateForce(agent);
        if (agent.getPosition().distanceTo(currentCheckpoint) < 10) {
            if (currentCheckpointIndex != checkpoints.size()-1) {
                currentCheckpointIndex++;
            }
            currentCheckpoint = checkpoints.get(currentCheckpointIndex);
        }
        return res;
    }
}
