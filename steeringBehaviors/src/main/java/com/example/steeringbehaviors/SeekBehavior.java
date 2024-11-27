package com.example.steeringbehaviors;

public class SeekBehavior extends Behavior {

    public SeekBehavior(Vector2D target) {
        this.setTarget(target);
    }

    @Override
    public Vector2D calculateForce(Agent agent) {
        Vector2D desired = this.getTarget().subtract(agent.getPosition()).normalize().scale(agent.getMaxSpeed());
        return desired.subtract(agent.getVelocity());
    }
}



