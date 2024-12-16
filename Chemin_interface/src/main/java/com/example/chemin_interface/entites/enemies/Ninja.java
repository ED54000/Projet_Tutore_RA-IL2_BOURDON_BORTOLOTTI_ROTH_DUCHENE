package com.example.chemin_interface.entites.enemies;

import com.example.chemin_interface.entites.defenses.Defense;

public class Ninja extends Ennemy{

    public Ninja(double x, double y) {
        super(x, y, 50, 0.05, 30, 2, 1.5, Integer.MAX_VALUE);
    }
}
