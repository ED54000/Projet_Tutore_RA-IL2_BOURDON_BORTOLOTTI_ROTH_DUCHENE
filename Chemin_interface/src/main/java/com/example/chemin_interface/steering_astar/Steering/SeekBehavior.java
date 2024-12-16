package com.example.chemin_interface.steering_astar.Steering;

public class SeekBehavior extends Behavior {

    /***
     * constructeur de la classe
     * @param target coordonnees de la cible du comportement
     */
    public SeekBehavior(Vector2D target) {
        this.setTarget(target);
    }

    /***
     * implementation du comportement seek, qui tend a atteindre une cible
     * @param agent l'agent sur lequel va etre applique la force
     * @return les coordonnees sur lesquelles va se baser le calcul de la velocite de l'agent
     */
    @Override
    public Vector2D calculateForce(Agent agent) {
        Vector2D desired = this.getTarget().subtract(agent.getPosition()).normalize().scale(agent.getMaxSpeed());
        return desired.subtract(agent.getVelocity());
    }
}



