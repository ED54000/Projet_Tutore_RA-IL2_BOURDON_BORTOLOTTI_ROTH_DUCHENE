package com.example.chemin_interface.entites.enemies;

import com.example.chemin_interface.steering_astar.Steering.Vector2D;

public class Giant extends Ennemy {

    public Giant(Vector2D position) {
        super(position, 100, 0.5, 200, 0.5, 1, 2);
    }
}
