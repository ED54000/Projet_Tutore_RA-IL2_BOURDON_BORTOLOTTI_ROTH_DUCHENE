package com.example.steering_astar.Steering;

import java.util.ArrayList;

public class PathfollowingBehavior extends Behavior {

    private ArrayList<Vector2D> checkpoints;
    private Vector2D currentCheckpoint;
    private int currentCheckpointIndex;

    public PathfollowingBehavior(ArrayList<Vector2D> checkpoints) {
        this.checkpoints = checkpoints;
        this.currentCheckpoint = this.checkpoints.getFirst();
        this.currentCheckpointIndex = 0;
        this.setTarget(currentCheckpoint);
    }

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
