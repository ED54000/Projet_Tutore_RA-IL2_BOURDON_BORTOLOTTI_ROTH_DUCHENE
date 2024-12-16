package com.example.chemin_interface.entites.defenses;

import com.example.chemin_interface.entites.enemies.Ennemy;

public class Canon extends ActiveDefense {

    public Canon(double x, double y) {
        super(x, y, 100, 10, 2.5, 1);
    }

    @Override
    public void attack(Ennemy target) {
        if (target != null) {
            target.takeDamage(getDamage()*getBonus(getType(), target.getType()));
        }
    }
}
