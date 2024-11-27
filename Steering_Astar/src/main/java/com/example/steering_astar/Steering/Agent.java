package com.example.steering_astar.Steering;

public class Agent {
    private Vector2D position;
    private Vector2D velocity;
    private double maxSpeed;
    private Behavior behavior;

    public Agent(Vector2D position, double maxSpeed) {
        this.position = position;
        this.velocity = new Vector2D(0, 0);
        this.maxSpeed = maxSpeed;
        this.behavior = null;
    }

    public void setBehavior(Behavior behavior) {
        this.behavior = behavior;
    }

    public void update() {
        if (behavior != null) {
            Vector2D steeringForce = behavior.calculateForce(this);
            velocity = velocity.add(steeringForce).normalize().scale(maxSpeed);
            position = position.add(velocity);
        }
    }

    public Vector2D getPosition() { return position; }
    public Vector2D getVelocity() { return velocity; }
    public double getMaxSpeed() { return maxSpeed; }
}

