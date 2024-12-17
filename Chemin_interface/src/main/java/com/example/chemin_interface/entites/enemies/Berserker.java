package com.example.chemin_interface.entites.enemies;

import com.example.chemin_interface.steering_astar.Steering.Vector2D;

public class Berserker extends Ennemy{

    public Berserker(Vector2D position) {
        super(position, 75, 0.25, 100, 1, 1, 3);
    }
}
