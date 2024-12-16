package com.example.chemin_interface.entites.enemies;

import com.example.chemin_interface.steering_astar.Steering.Vector2D;

public class Giant extends Ennemy {

    public Giant(Vector2D position) {
        super(position, 10, 0.025, 150, 1, 1, 10);
    }
}
