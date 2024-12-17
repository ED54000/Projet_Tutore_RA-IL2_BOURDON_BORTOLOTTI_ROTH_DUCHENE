package com.example.chemin_interface.entites.enemies;

import com.example.chemin_interface.steering_astar.Steering.Vector2D;

public class Druide extends Ennemy {
    public Druide(Vector2D position) {
        super(position, 3, 1, 100, 1, 1, 3);
    }
}
