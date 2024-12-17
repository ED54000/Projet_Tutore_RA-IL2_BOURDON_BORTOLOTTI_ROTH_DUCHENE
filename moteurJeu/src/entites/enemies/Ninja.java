package entites.enemies;

import entites.defenses.Defense;

public class Ninja extends Ennemy{

    public Ninja(double x, double y, String name) {
        super(x, y, 50, 0.05, 30, 2, 1.5, Integer.MAX_VALUE, name);
    }
}
