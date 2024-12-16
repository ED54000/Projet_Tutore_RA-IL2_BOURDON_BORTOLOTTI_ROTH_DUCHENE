package com.example.chemin_interface.entites.enemies;

import com.example.chemin_interface.entites.defenses.Defense;
import com.example.chemin_interface.steering_astar.Steering.Vector2D;

public class Ninja extends Ennemy{

    public Ninja(Vector2D position) {
        super(position, 30, 0.05, 50, 2, 1, Integer.MAX_VALUE);
    }
}
