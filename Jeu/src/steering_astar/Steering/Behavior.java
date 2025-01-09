package steering_astar.Steering;

import entites.enemies.Ennemy;

/***
 * classe abstraite definissant les differents comportements possibles,
 * qui ont tous une cible, et qui calculent la force a appliquer sur l'agent
 * de facon specifique
 */
public abstract class Behavior {

    private static final double TARGET_RADIUS = 50.0;
    protected final double ACCELERATION_DIVISER = 0.15;

    private Vector2D target;
    private double weight;

    /***
     * methode servant a calculer la force de mouvement selon un comportement
     * @param ennemy l'ennemi sur lequel va etre applique la force
     * @return les coordonnees sur lesquelles va se baser le calcul de la velocite de l'ennemi
     */
    public abstract Vector2D calculateForce(Ennemy ennemy);

    public void setTarget(Vector2D target) {
        this.target = target;
    }

    public Vector2D getTarget() {
         return this.target;
    }

    public static double getTargetRadius() {
        return TARGET_RADIUS;
    }
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}

