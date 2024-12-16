package com.example.chemin_interface.entites.defenses;

import com.example.chemin_interface.entites.enemies.Ennemy;
import com.example.chemin_interface.steering_astar.Steering.Vector2D;

public class Canon extends ActiveDefense {

    public Canon(Vector2D position) {
        super(position, 100, 10, 2.5, 1);
    }

    @Override
    public void attack(Ennemy target) {
        if (target != null) {
            target.takeDamage(getDamage()*getBonus(getType(), target.getType()));
        }
    }
}
