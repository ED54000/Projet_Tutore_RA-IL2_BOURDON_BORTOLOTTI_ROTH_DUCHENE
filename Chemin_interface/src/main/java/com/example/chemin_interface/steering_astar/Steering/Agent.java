package com.example.chemin_interface.steering_astar.Steering;

/***
 * classe definissant un agent mobile, par :
 *  - sa position a un instant t
 *  - sa velocite, qui permet de calculer sa postion a un instant t+1
 *  - sa vitesse maximale
 *  - son comportement
 */
public class Agent {
    private Vector2D position;
    private Vector2D velocity;
    private double maxSpeed;
    private Behavior behavior;

    /***
     * Constructeur de la classe
     * @param position coordonnees de l'agent
     * @param maxSpeed vitesse maximale de l'agent
     */
    public Agent(Vector2D position, double maxSpeed) {
        this.position = position;
        this.velocity = new Vector2D(0, 0);
        this.maxSpeed = maxSpeed;
        this.behavior = null;
    }

    /***
     * methode definissant la nouvelle position de l'agent grace a sa velocite, calculee selon le
     * comportement de l'agent
     */
//    public void update() {
//        if (behavior != null) {
//            Vector2D steeringForce = behavior.calculateForce(this);
//            velocity = velocity.add(steeringForce).normalize().scale(maxSpeed);
//            position = position.add(velocity);
//        }
//    }

    public Vector2D getPosition() { return position; }
    public Vector2D getVelocity() { return velocity; }
    public double getMaxSpeed() { return maxSpeed; }
    public Behavior getBehavior() { return behavior; }
    public void setBehavior(Behavior behavior) {
        this.behavior = behavior;
    }
}

