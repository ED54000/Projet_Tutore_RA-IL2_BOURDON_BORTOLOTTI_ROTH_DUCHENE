package com.example.chemin_interface.entites.defenses;

import com.example.chemin_interface.entites.enemies.Ennemy;

public class Bomb extends PassiveDefense{
    public Bomb(double x, double y) {
        super(x, y, 50, 1);
    }

    @Override
    public void attack(Ennemy target) {
        if (target != null) {
            target.takeDamage(getDamage()*getBonus(getType(), target.getType()));
        }
    }
}
