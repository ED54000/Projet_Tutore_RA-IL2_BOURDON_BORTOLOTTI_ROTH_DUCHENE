package com.example.proto_test;

public class ArrivalBehavior extends Behavior {

    private double slowingRadius;

    public ArrivalBehavior(Vector2D target, double slowingRadius) {
        this.setTarget(target);
        this.slowingRadius = slowingRadius;
    }

    @Override
    public Vector2D calculateForce(Agent agent) {
        Vector2D toTarget = this.getTarget().subtract(agent.getPosition());
        double distance = toTarget.magnitude();

        double desiredSpeed = (distance < slowingRadius)
                ? agent.getMaxSpeed() * (distance / slowingRadius)
                : agent.getMaxSpeed();

        Vector2D desired = toTarget.normalize().scale(desiredSpeed);
        return desired.subtract(agent.getVelocity());
    }
}

