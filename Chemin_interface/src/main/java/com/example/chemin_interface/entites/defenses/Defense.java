package com.example.chemin_interface.entites.defenses;

import com.example.chemin_interface.entites.Entity;
import com.example.chemin_interface.entites.enemies.Ennemy;

public abstract class Defense extends Entity {

    public Defense(double x, double y, int damage, double range) {
        super(x, y, damage, range);
    }

    public abstract void attack(Ennemy target);

}

