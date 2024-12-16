package com.example.chemin_interface.entites.defenses;

import com.example.chemin_interface.entites.Entity;
import com.example.chemin_interface.entites.enemies.Ennemy;
import com.example.chemin_interface.steering_astar.Steering.Vector2D;

public abstract class Defense extends Entity {

    public Defense(Vector2D position, int damage, double range) {
        super(position, damage, range);
    }

    public abstract void attack(Ennemy target);

}

