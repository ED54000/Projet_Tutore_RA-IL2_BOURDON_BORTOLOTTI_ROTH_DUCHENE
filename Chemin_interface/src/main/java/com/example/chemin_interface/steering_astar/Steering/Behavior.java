package com.example.chemin_interface.steering_astar.Steering;

/***
 * classe abstraite definissant les differents comportements possibles,
 * qui ont tous une cible, et qui calculent la force a appliquer sur l'agent
 * de facon specifique
 */
public abstract class Behavior {

    private Vector2D target;

    /***
     * methode servant a calculer la force de mouvement selon un comportement
     * @param agent l'agent sur lequel va etre applique la force
     * @return les coordonnees sur lesquelles va se baser le calcul de la velocite de l'agent
     */
    abstract Vector2D calculateForce(Agent agent);

    public void setTarget(Vector2D target) {
        this.target = target;
    }

    public Vector2D getTarget() {
         return this.target;
    }
}

